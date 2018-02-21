package cn.jiuling.recog.manager.util;

import java.awt.image.ImageProducer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.sun.jimi.core.Jimi;
import com.sun.jimi.core.JimiException;
import com.sun.jimi.core.JimiWriter;
import com.sun.jimi.core.options.JPGOptions;

public class ImageBaseUtil
{

    private static final Logger logger = Logger.getLogger(ImageBaseUtil.class);

    public static final byte[] input2byte(InputStream inStream)
            throws IOException
    {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0)
        {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        IOUtils.closeQuietly(swapStream);
        IOUtils.closeQuietly(inStream);
        return in2b;
    }

    /**
     * 获取图片格式函数
     * 
     * @param file
     * @return
     */
    public static String getExtension(InputStream input)
    {
        // 图片格式
        String format = "";
        ImageInputStream iis = null;

        try
        {
            iis = ImageIO.createImageInputStream(input);
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (iter.hasNext())
            {
                format = iter.next().getFormatName();
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (iis != null)
            {
                try
                {
                    iis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return format;
    }

    public static byte[] forJpg(InputStream input) throws IOException
    {
        try
        {
            ByteArrayOutputStream ots = new ByteArrayOutputStream();

            JPGOptions options = new JPGOptions();
            options.setQuality(100);

            ImageProducer image = Jimi.getImageProducer(input);
            JimiWriter writer = Jimi.createJimiWriter(Jimi.getEncoderTypes()[3],
                    ots);

            writer.setSource(image);
            writer.setOptions(options);
            writer.putImage(ots);

            return ots.toByteArray();
        }
        catch (JimiException e)
        {
            logger.error(e);
            e.printStackTrace();
        }
        return null;
    }
}
