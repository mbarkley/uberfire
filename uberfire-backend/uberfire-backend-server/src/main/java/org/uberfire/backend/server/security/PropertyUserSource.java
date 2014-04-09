package org.uberfire.backend.server.security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jboss.errai.security.shared.api.Role;
import org.jboss.errai.security.shared.api.RoleImpl;
import org.jboss.errai.security.shared.api.identity.User;
import org.jboss.errai.security.shared.api.identity.UserImpl;
import org.jboss.errai.security.shared.exception.FailedAuthenticationException;
import org.jboss.errai.security.shared.service.AuthenticationService;

public class PropertyUserSource implements AuthenticationService {

    private final Map<String, Object> credentials = new HashMap<String, Object>();
    private final Map<String, List<Role>> roles = new HashMap<String, List<Role>>();

    public PropertyUserSource( Properties properties ) {
        for ( Map.Entry<Object, Object> contentEntry : properties.entrySet() ) {
            final String content = contentEntry.getValue().toString();
            final String[] result = content.split( "," );
            credentials.put( contentEntry.getKey().toString(), result[ 0 ] );
            final List<Role> roles = new ArrayList<Role>();
            if ( result.length > 1 ) {
                for ( int i = 1; i < result.length; i++ ) {
                    final String currentRole = result[ i ];
                    roles.add( new RoleImpl( currentRole ) );
                }
                this.roles.put( contentEntry.getKey().toString(), roles );
            }
        }
    }

    @Override
    public User login( String username, String password ) {
        final Object pass = credentials.get( username );
        if ( pass != null && pass.equals( password ) ) {
            return new UserImpl( username, roles.get( username ) );
        }
        throw new FailedAuthenticationException();
    }

    @Override
    public boolean isLoggedIn() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void logout() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public User getUser() {
        throw new UnsupportedOperationException("Not implemented.");
    }
}