package org.mongodb.morphia.converters;


import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;
import org.mongodb.morphia.TestBase;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.testutil.TestEntity;


/**
 * @author Uwe Schaefer, (us@thomas-daily.de)
 */
public class EnumSetTest extends TestBase {
  public enum NastyEnum {
    A {
      @Override
      public String toString() {
        return "Never use toString for other purposes than debugging";
      }
    },
    B {
      public String toString() {
        return "Never use toString for other purposes than debugging ";
      }
    },
    C,
    D
  }

  public static class NastyEnumEntity extends TestEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    final EnumSet<NastyEnum> in    = EnumSet.of(NastyEnum.B, NastyEnum.C, NastyEnum.D);
    final EnumSet<NastyEnum> out   = EnumSet.of(NastyEnum.A);
    final EnumSet<NastyEnum> empty = EnumSet.noneOf(NastyEnum.class);
    EnumSet<NastyEnum> isNull;
  }

  @Test
  public void testNastyEnumPersistence() throws Exception {
    NastyEnumEntity n = new NastyEnumEntity();
    ds.save(n);
    n = ds.get(n);

    Assert.assertNull(n.isNull);
    Assert.assertNotNull(n.empty);
    Assert.assertNotNull(n.in);
    Assert.assertNotNull(n.out);

    Assert.assertEquals(0, n.empty.size());
    Assert.assertEquals(3, n.in.size());
    Assert.assertEquals(1, n.out.size());

    Assert.assertTrue(n.in.contains(NastyEnum.B));
    Assert.assertTrue(n.in.contains(NastyEnum.C));
    Assert.assertTrue(n.in.contains(NastyEnum.D));
    Assert.assertFalse(n.in.contains(NastyEnum.A));

    Assert.assertTrue(n.out.contains(NastyEnum.A));
    Assert.assertFalse(n.out.contains(NastyEnum.B));
    Assert.assertFalse(n.out.contains(NastyEnum.C));
    Assert.assertFalse(n.out.contains(NastyEnum.D));

    Query<NastyEnumEntity> q = ds.find(NastyEnumEntity.class, "in", NastyEnum.C);
    Assert.assertEquals(1, q.countAll());
    q = ds.find(NastyEnumEntity.class, "out", NastyEnum.C);
    Assert.assertEquals(0, q.countAll());

  }
}
