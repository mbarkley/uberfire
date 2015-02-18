/*
   Copyright (c) 2014,2015 Ahome' Innovation Technologies. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.ait.lienzo.client.core.event;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractNodeMouseEvent<T extends MouseEvent<?>, H extends EventHandler> extends AbstractNodeHumanInputEvent<T, H> implements INodeXYEvent
{
    private final int m_x;

    private final int m_y;

    public static class Type<H> extends GwtEvent.Type<H>
    {
    }

    protected AbstractNodeMouseEvent(final T event)
    {
        super(event);

        m_x = event.getRelativeX(event.getRelativeElement());

        m_y = event.getRelativeY(event.getRelativeElement());
    }

    protected AbstractNodeMouseEvent(final T event, final int x, final int y)
    {
        super(event);

        m_x = x;

        m_y = y;
    }

    @Override
    public int getX()
    {
        return m_x;
    }

    @Override
    public int getY()
    {
        return m_y;
    }

    @Override
    public GwtEvent<?> getNodeEvent()
    {
        return this;
    }

    /**
     * The native event that was initially generated by the DOM.
     * 
     * @return
     */
    public final T getMouseEvent()
    {
        return getHumanInputEvent();
    }

    public final boolean isButtonLeft()
    {
        return isButtonLeft(getMouseEvent());
    }

    public static final boolean isButtonLeft(final MouseEvent<?> event)
    {
        if (null != event)
        {
            if (event.getNativeButton() == NativeEvent.BUTTON_LEFT)
            {
                return true;
            }
        }
        return false;
    }

    public final boolean isButtonMiddle()
    {
        return isButtonMiddle(getMouseEvent());
    }

    public static final boolean isButtonMiddle(final MouseEvent<?> event)
    {
        if (null != event)
        {
            if (event.getNativeButton() == NativeEvent.BUTTON_MIDDLE)
            {
                return true;
            }
        }
        return false;
    }

    public final boolean isButtonRight()
    {
        return isButtonRight(getMouseEvent());
    }

    public static final boolean isButtonRight(final MouseEvent<?> event)
    {
        if (null != event)
        {
            if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT)
            {
                return true;
            }
        }
        return false;
    }
}
