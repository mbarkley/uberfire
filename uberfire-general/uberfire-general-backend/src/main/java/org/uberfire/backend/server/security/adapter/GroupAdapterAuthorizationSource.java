/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.backend.server.security.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;
import javax.security.auth.Subject;

import org.jboss.errai.security.shared.api.Group;
import org.jboss.errai.security.shared.api.GroupImpl;
import org.jboss.errai.security.shared.api.Role;
import org.jboss.errai.security.shared.api.RoleImpl;
import org.uberfire.backend.server.security.RoleRegistry;
import org.uberfire.security.authz.adapter.GroupsAdapter;

public class GroupAdapterAuthorizationSource {

    private final ServiceLoader<GroupsAdapter> groupsAdapterServiceLoader = ServiceLoader.load(GroupsAdapter.class);

    protected List<String> loadEntitiesFromSubjectAndAdapters(String username,
                                                              Subject subject,
                                                              String[] rolePrincipleNames) {
        List<String> roles = new ArrayList<>();
        try {
            List<String> principals = collectEntitiesFromSubject(username,
                                                                 subject,
                                                                 rolePrincipleNames);
            roles.addAll(filterValidPrincipals(principals));
            List<String> principalsFromAdapters = collectEntitiesFromAdapters(username,
                                                                              subject);
            roles.addAll(filterValidPrincipals(principalsFromAdapters));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return roles;
    }

    private List<String> filterValidPrincipals(List<String> principals) {
        if (principals == null) {
            return new ArrayList<>();
        }
        return principals.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    protected List<String> collectEntitiesFromAdapters(String username,
                                                       Subject subject) {
        Set<String> userGroups = new HashSet<String>();
        for (final GroupsAdapter adapter : groupsAdapterServiceLoader) {
            final List<Group> groupRoles = adapter.getGroups(username,
                                                             subject);
            if (groupRoles != null) {
                for (Group group : groupRoles) {
                    userGroups.add(group.getName());
                }
            }
        }

        return new LinkedList<String>(userGroups);
    }

    /**
     * Collects the principals for a given subject.
     */
    protected List<String> collectEntitiesFromSubject(String username,
                                                      Subject subject,
                                                      String[] rolePrincipleNames) {
        if (null == subject) {
            return null;
        }

        List<String> roles = new ArrayList<String>();
        try {
            Set<java.security.Principal> principals = subject.getPrincipals();
            if (principals != null) {
                for (java.security.Principal p : principals) {
                    if (p instanceof java.security.acl.Group) {
                        for (final String rolePrincipleName : rolePrincipleNames) {
                            if (rolePrincipleName.equalsIgnoreCase(p.getName())) {
                                Enumeration<? extends java.security.Principal> groups = ((java.security.acl.Group) p).members();
                                while (groups.hasMoreElements()) {
                                    final java.security.Principal groupPrincipal = groups.nextElement();
                                    roles.add(groupPrincipal.getName());
                                }
                            }
                        }
                    } else {
                        roles.add(p.getName());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return roles;
    }

    /**
     * For a given collection of principal names, return the Role instances for the ones
     * that are considered roles, so the ones that exist on the RoleRegistry.
     */
    protected List<Role> getRoles(List<String> principals) {

        if (null != principals && !principals.isEmpty()) {

            Set<Role> registeredRoles = RoleRegistry.get().getRegisteredRoles();

            if (null != registeredRoles && !registeredRoles.isEmpty()) {

                List<Role> result = new LinkedList<Role>();

                for (String role : principals) {

                    if (null != RoleRegistry.get().getRegisteredRole(role)) {

                        result.add(new RoleImpl(role));
                    }
                }

                return result;
            }
        }

        return Collections.emptyList();
    }

    /**
     * For a given collection of principal names, return the Role instances for the ones
     * that are considered roles, so the ones that exist on the RoleRegistry.
     */
    protected List<Group> getGroups(List<String> principals) {

        if (null != principals && !principals.isEmpty()) {

            Set<Role> registeredRoles = RoleRegistry.get().getRegisteredRoles();

            if (null != registeredRoles && !registeredRoles.isEmpty()) {

                List<Group> result = new LinkedList<Group>();

                for (String role : principals) {

                    if (null == RoleRegistry.get().getRegisteredRole(role)) {

                        result.add(new GroupImpl(role));
                    }
                }

                return result;
            }
        }

        return Collections.emptyList();
    }
}