/*
 * $RCSfile: RawTileDecoder.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.1 $
 * $Date: 2005-02-11 04:56:58 $
 * $State: Exp $
 */package com.sun.media.jai.tilecodec;

import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.Raster;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import javax.media.jai.JAI;
import javax.media.jai.ParameterListDescriptor;
import javax.media.jai.tilecodec.TileCodecParameterList;
import javax.media.jai.tilecodec.TileDecoderImpl;
import javax.media.jai.util.ImagingListener;
import com.sun.media.jai.util.ImageUtil;

/**
 * A concrete implementation of the <code>TileDecoderImpl</code> class
 * for the raw tile codec.
 */
public class RawTileDecoder extends TileDecoderImpl {
    /**
     * Constructs a <code>RawTileDecoder</code>.
     * <code>RawTileDecoder</code> may throw a
     * <code>IllegalArgumentException</code> if <code>param</code>'s
     * <code>getParameterListDescriptor()</code> method does not return
     * the same descriptor as that from the associated
     * <code>TileCodecDescriptor</code>'s
     * <code>getParameterListDescriptor</code> method for the "tileDecoder"
     * registry mode.
     *
     * <p> If param is null, then the default parameter list for decoding
     * as defined by the associated <code>TileCodecDescriptor</code>'s
     * <code>getDefaultParameters()</code> method will be used for decoding.
     *
     * @param input The <code>InputStream</code> to decode data from.
     * @param param  The object containing the tile decoding parameters.
     * @throws IllegalArgumentException if input is null.
     * @throws IllegalArgumentException if param is not appropriate.
     */
    public RawTileDecoder(InputStream input, TileCodecParameterList param) {
	super("raw", input, param);
    }

    /**
     * Returns a <code>Raster</code> that contains the decoded contents
     * of the <code>InputStream</code> associated with this
     * <code>TileDecoder</code>.
     *
     * <p>This method can perform the decoding correctly only when
     * <code>includesLocationInfo()</code> returns true.
     *
     * @throws IOException if an I/O error occurs while reading from the
     * associated InputStream.
     * @throws IllegalArgumentException if the associated
     * TileCodecDescriptor's includesLocationInfo() returns false.
     */
    public Raster decode() throws IOException{

	ObjectInputStream ois = new ObjectInputStream(inputStream);

	try {
	    Object object = ois.readObject();
	    return TileCodecUtils.deserializeRaster(object);
	}
	catch (ClassNotFoundException e) {
            ImagingListener listener =
                ImageUtil.getImagingListener((RenderingHints)null);
            listener.errorOccurred(JaiI18N.getString("ClassNotFound"),
                                   e, this, false);
//            e.printStackTrace();
	    return null;
	}
	finally {
	    ois.close();
	}
    }

    public Raster decode(Point location) throws IOException{
        return decode();
    }
}

