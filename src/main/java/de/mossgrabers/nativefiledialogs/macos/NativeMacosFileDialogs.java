// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.nativefiledialogs.macos;

import de.mossgrabers.nativefiledialogs.FileFilter;
import de.mossgrabers.nativefiledialogs.NativeFileDialogs;

import java.io.File;


/**
 * The Macos implementation for the file dialogs.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class NativeMacosFileDialogs implements NativeFileDialogs
{
    /**
     * Creates a new file dialog instance with the initial directory.
     *
     * @param currentDirectory The initial directory, may be null
     */
    public NativeMacosFileDialogs (final File currentDirectory)
    {
        // TODO Auto-generated constructor stub
    }


    /** {@inheritDoc} */
    @Override
    public File selectFile (final FileFilter... filters)
    {
        // TODO Auto-generated method stub
        return null;
    }


    /** {@inheritDoc} */
    @Override
    public File selectNewFile (final FileFilter... filters)
    {
        // TODO Auto-generated method stub
        return null;
    }


    /** {@inheritDoc} */
    @Override
    public File selectFolder (final String title)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
