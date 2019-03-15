// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.nativefiledialogs.linux;

import de.mossgrabers.nativefiledialogs.AbstractNativeFileDialogs;
import de.mossgrabers.nativefiledialogs.FileFilter;

import java.io.File;
import java.io.IOException;


/**
 * The Linux implementation for the file dialogs.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class NativeLinuxFileDialogs extends AbstractNativeFileDialogs
{
    /**
     * Creates a new file dialog instance with the initial directory.
     *
     * @param currentDirectory The initial directory, may be null
     */
    public NativeLinuxFileDialogs (final File currentDirectory)
    {
        // TODO Auto-generated constructor stub
    }


    /** {@inheritDoc} */
    @Override
    public File selectFile (String title, FileFilter... filters) throws IOException
    {
        // TODO Auto-generated method stub
        return null;
    }


    /** {@inheritDoc} */
    @Override
    public File selectNewFile (String title, FileFilter... filters) throws IOException
    {
        // TODO Auto-generated method stub
        return null;
    }


    /** {@inheritDoc} */
    @Override
    public File selectFolder (String title) throws IOException
    {
        // TODO Auto-generated method stub
        return null;
    }
}
