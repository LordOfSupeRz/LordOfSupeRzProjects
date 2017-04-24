package heroz.api.enitities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;

import net.minecraft.server.v1_7_R4.BiomeBase;
import net.minecraft.server.v1_7_R4.BiomeMeta;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityInsentient;

public enum EntityTypes
{
	 CUSTOM_VILLAGER("Villager", 120 , VillagerEntity.class); 
	    private EntityTypes(String name, int id, Class<? extends Entity> custom)
	    {
	        addToMaps(custom, name, id);
	    }
	    


	  public static void spawnEntity(Entity entity, Location loc)
	   {
	     entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		
	     ((CraftWorld)loc.getWorld()).getHandle().addEntity(entity);
	   }


	  public static void registerEntity(String name, int id, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass) {
		  try {
		   

		  List<Map<?, ?>> dataMaps = new ArrayList<Map<?, ?>>();
		  for (Field f : EntityTypes.class.getDeclaredFields()) {
		  if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
		  f.setAccessible(true);
		  dataMaps.add((Map<?, ?>) f.get(null));
		  } 
		  }

		  if (dataMaps.get(2).containsKey(id)) {
		  dataMaps.get(0).remove(name);
		  dataMaps.get(2).remove(id);
		  }

		  Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
		  method.setAccessible(true);
		  method.invoke(null, customClass, name, id);

		  for (Field f : BiomeBase.class.getDeclaredFields()) {
		  if (f.getType().getSimpleName().equals(BiomeBase.class.getSimpleName())) {
		  if (f.get(null) != null) {

		  for (Field list : BiomeBase.class.getDeclaredFields()) {
		  if (list.getType().getSimpleName().equals(List.class.getSimpleName())) {
		  list.setAccessible(true);
		  @SuppressWarnings("unchecked")
		  List<BiomeMeta> metaList = (List<BiomeMeta>) list.get(f.get(null));
	
		  for (BiomeMeta meta : metaList) {
		  Field clazz = BiomeMeta.class.getDeclaredFields()[0];
		  if (clazz.get(meta).equals(nmsClass)) {
		  clazz.set(meta, customClass);
		  }
		  }
		  }
		  }
		   
		  }
		  }
		  }
		   
		  } catch (Exception e) {
		  }
		  }
	    @SuppressWarnings({ "unchecked", "rawtypes" })
		private static void addToMaps(Class clazz, String name, int id)
	    {
            ((Map)getPrivateField("c", net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(name, clazz);
            ((Map) getPrivateField("d", net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(clazz, name);
            ((Map)getPrivateField("e", net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(Integer.valueOf(id), clazz);
            ((Map) getPrivateField("f", net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(clazz, Integer.valueOf(id));
            ((Map)getPrivateField("g", net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(name, Integer.valueOf(id));
	    }
	    @SuppressWarnings("rawtypes")
		public static Object getPrivateField(String fieldName, Class clazz, Object object)
	    {
	        Field field;
	        Object o = null;
	        try
	        {
	            field = clazz.getDeclaredField(fieldName);
	            field.setAccessible(true);
	            o = field.get(object);
	        }
	        catch(NoSuchFieldException e)
	        {
	            e.printStackTrace();
	        }
	        catch(IllegalAccessException e)
	        {
	            e.printStackTrace();
	        }
	        return o;
	    }
}