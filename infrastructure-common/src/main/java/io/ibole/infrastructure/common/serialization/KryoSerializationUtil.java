package io.ibole.infrastructure.common.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoCallback;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;

import org.objenesis.strategy.StdInstantiatorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
 *********************************************************************************************/

/**
 * Kryo serialize/deserialize helper.
 * 
 * @author bwang
 *
 */
public final class KryoSerializationUtil {

  private static final Logger logger = LoggerFactory.getLogger(KryoSerializationUtil.class);

  static final List<String> SINGLETON_LIST = Collections.singletonList("");
  static final Set<String> SINGLETON_SET = Collections.singleton("");
  static final Map<String, String> SINGLETON_MAP = Collections.singletonMap("", "");
  static final Set<String> SET_FROM_MAP = Collections.newSetFromMap(new HashMap<String, Boolean>());

  public static KryoFactory factory;
  public static KryoPool pool;

  private static class SerializationHolder {
    private static final KryoSerializationUtil instance = new KryoSerializationUtil();
  }

  public static KryoSerializationUtil getInstance() {
    return SerializationHolder.instance;
  }

  private KryoSerializationUtil() {
    // do nothing.
  }
    
  /**
   * Serialization with kryo.
   * 
   * @return the instance of KryoFactory
   */
  private KryoFactory getKryoFactory() {
    if (factory != null) {
      return factory;
    }
    factory = new KryoFactory() {

      public Kryo create() {
        Kryo kryo = new Kryo();
        try {
          kryo.register(boolean[].class);
          kryo.register(byte[].class);
          kryo.register(short[].class);
          kryo.register(char[].class);
          kryo.register(int[].class);
          kryo.register(float[].class);
          kryo.register(long[].class);
          kryo.register(double[].class);
          kryo.register(String[].class);
          kryo.register(Timestamp.class, new TimestampSerializer());
          //kryo.register(Date.class, new DateSerializer(Date.class));
          //kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
          //kryo.register(Collections.EMPTY_LIST.getClass(), new CollectionsEmptyListSerializer());
          //kryo.register(Collections.EMPTY_MAP.getClass(), new CollectionsEmptyMapSerializer());
          //kryo.register(Collections.EMPTY_SET.getClass(), new CollectionsEmptySetSerializer());
          //kryo.register(SINGLETON_LIST.getClass(), new CollectionsSingletonListSerializer());
          //kryo.register(SINGLETON_SET.getClass(), new CollectionsSingletonSetSerializer());
          //kryo.register(SINGLETON_MAP.getClass(), new CollectionsSingletonMapSerializer());
          kryo.setRegistrationRequired(false);

          // 动态注册JodaDateTimeSerializer
          try {
            Class<?> clazz = Class.forName("org.joda.time.DateTime");
            Serializer<?> serializer =
                (Serializer<?>) Class.forName(
                    "de.javakaffee.kryoserializers.jodatime.JodaDateTimeSerializer").newInstance();
            kryo.register(clazz, serializer);
          } catch (Throwable thex) {
            logger.error("Exception occurred", thex);
          }

          // 动态注册CGLibProxySerializer
//          try {
//            Class<?> clazz =
//                Class
//                    .forName("de.javakaffee.kryoserializers.cglib.CGLibProxySerializer$CGLibProxyMarker");
//            Serializer<?> serializer =
//                (Serializer<?>) Class.forName(
//                    "de.javakaffee.kryoserializers.cglib.CGLibProxySerializer").newInstance();
//            kryo.register(clazz, serializer);
//          } catch (Throwable thex) {
//            logger.error("Exception occurred", thex);
//          }

          //UnmodifiableCollectionsSerializer.registerSerializers(kryo);
         // SynchronizedCollectionsSerializer.registerSerializers(kryo);

          ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy())
              .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
        } catch (Exception e) {
          logger.error("Exception occurred", e);
        }
        return kryo;
      }
    };
    return factory;
  }

  /**
   * Get KryoPool.
   * 
   * @return the instance of KryoPool
   */
  private KryoPool getKryoPool() {
    if (pool != null) {
      return pool;
    }
    // Build pool with SoftReferences enabled (optional)
    pool = new KryoPool.Builder(getKryoFactory()).softReferences().build();
    return pool;
  }

  /**
   * Serialize Object.
   * 
   * @param obj the object to be serialized
   * @return the serialized array of byte
   */
  public byte[] serialize(final Object obj) {

    return getKryoPool().run(new KryoCallback<byte[]>() {

      public byte[] execute(Kryo kryo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Output output = new Output(stream);
        kryo.writeClassAndObject(output, obj);
        output.close();
        return stream.toByteArray();
      }

    });
  }

  /**
   * Deserialize the array of byte data to object.
   * 
   * @param objectData the array of byte
   * @return the deserialized object
   */
  @SuppressWarnings("unchecked")
  public <V> V deserialize(final byte[] objectData) {

    return (V) getKryoPool().run(new KryoCallback<V>() {

      public V execute(Kryo kryo) {
        Input input = new Input(objectData);
        return (V) kryo.readClassAndObject(input);
      }

    });
  }

  /**
   * Deep Copy from a specified object.
   * 
   * @param obj the source object to be copied
   * @return the target object copied from the original object
   */
  public <V> V deepCopy(final V obj) {

    return (V) getKryoPool().run(new KryoCallback<V>() {

      public V execute(Kryo kryo) {
        return (V) kryo.copy(obj);
      }

    });
  }

  /**
   * Serializes instances of {@link java.sql.Timestamp}.
   * 
   */
  public class TimestampSerializer extends Serializer<Timestamp> {

    /**
     * {@inheritDoc}.
     */
    @Override
    public void write(Kryo kryo, Output output, Timestamp object) {
      output.writeLong(object.getTime(), true);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public Timestamp read(Kryo kryo, Input input, Class<Timestamp> type) {
      return new Timestamp(input.readLong(true));
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public Timestamp copy(Kryo kryo, Timestamp original) {
      return new Timestamp(original.getTime());
    }

  }

}
