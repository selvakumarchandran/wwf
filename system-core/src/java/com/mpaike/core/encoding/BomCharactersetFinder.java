/*
 * Copyright (C) 2009-2010 WWF Software Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

 * As a special exception to the terms and conditions of version 2.0 of 
 * the GPL, you may redistribute this Program in connection with Free/Libre 
 * and Open Source Software ("FLOSS") applications as described in WWF's 
 * FLOSS exception.  You should have recieved a copy of the text describing 
 * the FLOSS exception, and it is also available here: 
 * http://www.42y.net/legal/licensing"
 */
package com.mpaike.core.encoding;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Byte Order Marker encoding detection.
 * 
 */
public class BomCharactersetFinder extends AbstractCharactersetFinder
{
    private static Log logger = LogFactory.getLog(BomCharactersetFinder.class);
    
    @Override
    public void setBufferSize(int bufferSize)
    {
        logger.warn("Setting the buffersize has no effect for charset finder: " + BomCharactersetFinder.class.getName());
    }

    /**
     * @return          Returns 64
     */
    @Override
    protected int getBufferSize()
    {
        return 64;
    }

    /**
     * Just searches the Byte Order Marker, i.e. the first three characters for a sign of
     * the encoding.
     */
    protected Charset detectCharsetImpl(byte[] buffer) throws Exception
    {
        Charset charset = null;
        ByteArrayInputStream bis = null;
        try
        {
            bis = new ByteArrayInputStream(buffer);
            bis.mark(3);
            char[] byteHeader = new char[3];
            InputStreamReader in = new InputStreamReader(bis);
            int bytesRead = in.read(byteHeader);
            bis.reset();

            if (bytesRead < 2)
            {
                // ASCII
                charset = Charset.forName("Cp1252");
            }
            else if (
                    byteHeader[0] == 0xFE &&
                    byteHeader[1] == 0xFF)
            {
                // UCS-2 Big Endian
                charset = Charset.forName("UTF-16BE");
            }
            else if (
                    byteHeader[0] == 0xFF &&
                    byteHeader[1] == 0xFE)
            {
                // UCS-2 Little Endian
                charset = Charset.forName("UTF-16LE");
            }
            else if (
                    bytesRead >= 3 &&
                    byteHeader[0] == 0xEF &&
                    byteHeader[1] == 0xBB &&
                    byteHeader[2] == 0xBF)
            {
                // UTF-8
                charset = Charset.forName("UTF-8");
            }
            else
            {
                // No idea
                charset = null;
            }
            // Done
            return charset;
        }
        finally
        {
            if (bis != null)
            {
                try { bis.close(); } catch (Throwable e) {}
            }
        }
    }
}
