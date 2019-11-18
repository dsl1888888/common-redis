package com.singleandmasterslave.xyjr.redis.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SerializeUtil
{
    public static byte[] serialize(Object object)
    {
        if (object == null)
        {
            return null;
        }
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        // bytes = null;
        try
        {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                baos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                oos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }

    public static Object unserialize(byte[] bytes)
    {
        if (bytes == null)
        {
            return null;
        }
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try
        {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                ois.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                bais.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] serializeList(List<?> list)
    {
        if ((list == null) || (list.isEmpty()))
        {
            return null;
        }
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        // bytes = null;
        try
        {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            for (Object obj : list)
            {
                oos.writeObject(obj);
            }
            return baos.toByteArray();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                oos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                baos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }

    @SuppressWarnings("unchecked")
    public static List<?> unserializeList(byte[] bytes)
    {
        if (bytes == null)
        {
            return null;
        }
        @SuppressWarnings("rawtypes")
        List<Object> list = new ArrayList();
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try
        {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            while (bais.available() > 0)
            {
                Object obj = ois.readObject();
                if (obj == null)
                {
                    break;
                }
                list.add(obj);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                bais.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                ois.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static void close(Closeable closeable)
    {
        if (closeable != null)
        {
            try
            {
                closeable.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
