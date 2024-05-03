package utils;

/*  1:   */ 
/*  2:   */ 
/*  3:   */ import java.io.ByteArrayInputStream;
/*  4:   */ import java.io.ByteArrayOutputStream;
/*  5:   */ import java.io.IOException;
/*  6:   */ import java.io.ObjectInputStream;
/*  7:   */ import java.io.ObjectOutputStream;
/*  8:   */ 
/*  9:   */ public class Convert
/* 10:   */ {
	/* 11:   */   public static byte[] toBytes(Object object)
			/* 12:   */     throws IOException
	/* 13:   */   {
		/* 14:12 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
		/* 15:13 */     ObjectOutputStream oos = new ObjectOutputStream(baos);
		/* 16:14 */     oos.writeObject(object);
		/* 17:15 */     return baos.toByteArray();
	/* 18:   */   }
	/* 19:   */   
	/* 20:   */   public static Object toObject(byte[] bytes)
			/* 21:   */     throws IOException, ClassNotFoundException
	/* 22:   */   {
		/* 23:20 */     Object object = null;
		/* 24:21 */     object = new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
		/* 25:22 */     return object;
	/* 26:   */   }
/* 27:   */ }