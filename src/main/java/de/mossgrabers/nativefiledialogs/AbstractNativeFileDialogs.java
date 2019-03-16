// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.nativefiledialogs;

import java.io.File;
import java.io.IOException;


/**
 * Abstract implementation of the file selection dialogs.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public abstract class AbstractNativeFileDialogs implements NativeFileDialogs
{
    /** {@inheritDoc} */
    @Override
    public File selectFile () throws IOException
    {
        return this.selectFile ((String) null);
    }


    /** {@inheritDoc} */
    @Override
    public File selectFile (FileFilter... filters) throws IOException
    {
        return this.selectFile ((String) null, filters);
    }


    /** {@inheritDoc} */
    @Override
    public File selectNewFile () throws IOException
    {
        return this.selectNewFile ((String) null);
    }


    /** {@inheritDoc} */
    @Override
    public File selectNewFile (FileFilter... filters) throws IOException
    {
        return this.selectNewFile ((String) null, filters);
    }


    /** {@inheritDoc} */
    @Override
    public File selectFolder () throws IOException
    {
        return this.selectFolder ((String) null);
    }
}
