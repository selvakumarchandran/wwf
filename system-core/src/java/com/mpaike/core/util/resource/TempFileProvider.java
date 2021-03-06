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
package com.mpaike.core.util.resource;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mpaike.core.error.WWFRuntimeException;

/**
 * A helper class that provides temp files, providing a common point to clean
 * them up.
 */
public class TempFileProvider
{
    /** subdirectory in the temp directory where WWF temporary files will go */
    public static final String WWF_TEMP_FILE_DIR = "WWF";

    /** the system property key giving us the location of the temp directory */
    public static final String SYSTEM_KEY_TEMP_DIR = "java.io.tmpdir";

    private static final Log logger = LogFactory.getLog(TempFileProvider.class);

    /**
     * Static class only
     */
    private TempFileProvider()
    {
    }

    /**
     * @return Returns the system temporary directory i.e. <code>isDir == true</code>
     */
    public static File getSystemTempDir()
    {
        String systemTempDirPath = System.getProperty(SYSTEM_KEY_TEMP_DIR);
        if (systemTempDirPath == null)
        {
            throw new WWFRuntimeException("System property not available: " + SYSTEM_KEY_TEMP_DIR);
        }
        File systemTempDir = new File(systemTempDirPath);
        return systemTempDir;
    }
    
    /**
     * @return Returns a temporary directory, i.e. <code>isDir == true</code>
     */
    public static File getTempDir()
    {
        File systemTempDir = getSystemTempDir();
        // append the WWF directory
        File tempDir = new File(systemTempDir, WWF_TEMP_FILE_DIR);
        // ensure that the temp directory exists
        if (tempDir.exists())
        {
            // nothing to do
        }
        else
        {
            // not there yet
            if (!tempDir.mkdirs())
            {
                throw new WWFRuntimeException("Failed to create temp directory: " + tempDir);
            }
            if (logger.isDebugEnabled())
            {
                logger.debug("Created temp directory: " + tempDir);
            }
        }
        // done
        return tempDir;
    }

    /**
     * @return Returns a temp <code>File</code> that will be located in the
     *         <b>WWF</b> subdirectory of the default temp directory
     * 
     * @see #WWF_TEMP_FILE_DIR
     * @see File#createTempFile(java.lang.String, java.lang.String)
     */
    public static File createTempFile(String prefix, String suffix)
    {
        File tempDir = TempFileProvider.getTempDir();
        // we have the directory we want to use
        return createTempFile(prefix, suffix, tempDir);
    }

    /**
     * @return Returns a temp <code>File</code> that will be located in the
     *         given directory
     * 
     * @see #WWF_TEMP_FILE_DIR
     * @see File#createTempFile(java.lang.String, java.lang.String)
     */
    public static File createTempFile(String prefix, String suffix, File directory)
    {
        try
        {
            File tempFile = File.createTempFile(prefix, suffix, directory);
            return tempFile;
        } catch (IOException e)
        {
            throw new WWFRuntimeException("Failed to created temp file: \n" +
                    "   prefix: " + prefix + "\n"
                    + "   suffix: " + suffix + "\n" +
                    "   directory: " + directory,
                    e);
        }
    }

    /**
     * Cleans up <b>all</b> WWF temporary files that are older than the
     * given number of hours.  Subdirectories are emptied as well and all directories
     * below the primary temporary subdirectory are removed.
     * <p>
     * The job data must include a property <tt>protectHours</tt>, which is the
     * number of hours to protect a temporary file from deletion since its last
     * modification.
     * 
     * @author Derek Hulley
     */
    public static class TempFileCleanerJob implements Job
    {
        public static final String KEY_PROTECT_HOURS = "protectHours";

        /**
         * Gets a list of all files in the {@link TempFileProvider#WWF_TEMP_FILE_DIR temp directory}
         * and deletes all those that are older than the given number of hours.
         */
        public void execute(JobExecutionContext context) throws JobExecutionException
        {
            // get the number of hours to protect the temp files
            String strProtectHours = (String) context.getJobDetail().getJobDataMap().get(KEY_PROTECT_HOURS);
            if (strProtectHours == null)
            {
                throw new JobExecutionException("Missing job data: " + KEY_PROTECT_HOURS);
            }
            int protectHours = -1;
            try
            {
                protectHours = Integer.parseInt(strProtectHours);
            }
            catch (NumberFormatException e)
            {
                throw new JobExecutionException("Invalid job data " + KEY_PROTECT_HOURS + ": " + strProtectHours);
            }
            if (protectHours < 0 || protectHours > 8760)
            {
                throw new JobExecutionException("Hours to protect temp files must be 0 <= x <= 8760");
            }

            long now = System.currentTimeMillis();
            long aFewHoursBack = now - (3600L * 1000L * protectHours);
            
            File tempDir = TempFileProvider.getTempDir();
            int count = removeFiles(tempDir, aFewHoursBack, false);  // don't delete this directory
            // done
            if (logger.isDebugEnabled())
            {
                logger.debug("Removed " + count + " files from temp directory: " + tempDir);
            }
        }
        
        /**
         * Removes all temporary files created before the given time.
         * <p>
         * The delete will cascade down through directories as well.
         * 
         * @param removeBefore only remove files created <b>before</b> this time
         * @return Returns the number of files removed
         */
        public static int removeFiles(long removeBefore)
        {
            File tempDir = TempFileProvider.getTempDir();
            return removeFiles(tempDir, removeBefore, false);
        }
        
        /**
         * @param directory the directory to clean out - the directory will optionally be removed
         * @param removeBefore only remove files created <b>before</b> this time
         * @param removeDir true if the directory must be removed as well, otherwise false
         * @return Returns the number of files removed
         */
        private static int removeFiles(File directory, long removeBefore, boolean removeDir)
        {
            if (!directory.isDirectory())
            {
                throw new IllegalArgumentException("Expected a directory to clear: " + directory);
            }
            // check if there is anything to to
            if (!directory.exists())
            {
                return 0;
            }
            // list all files
            File[] files = directory.listFiles();
            int count = 0;
            for (File file : files)
            {
                if (file.isDirectory())
                {
                    // enter subdirectory and clean it out and remove it
                    removeFiles(file, removeBefore, true);
                }
                else
                {
                    // it is a file - check the created time
                    if (file.lastModified() > removeBefore)
                    {
                        // file is not old enough
                        continue;
                    }
                    // it is a file - attempt a delete
                    try
                    {
                        file.delete();
                        count++;
                    }
                    catch (Throwable e)
                    {
                        logger.info("Failed to remove temp file: " + file);
                    }
                }
            }
            // must we delete the directory we are in?
            if (removeDir && directory.listFiles().length == 0)
            {
                // the directory must be removed and is empty
                try
                {
                    directory.delete();
                }
                catch (Throwable e)
                {
                    logger.info("Failed to remove temp directory: " + directory);
                }
            }
            // done
            return count;
        }
    }
}
