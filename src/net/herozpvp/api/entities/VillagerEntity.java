 package net.herozpvp.api.entities;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_7_R4.DamageSource;
import net.minecraft.server.v1_7_R4.EntityVillager;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R4.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R4.PathfinderGoalSelector;

@SuppressWarnings("rawtypes")
public class VillagerEntity extends EntityVillager {
	public boolean save;
	 public VillagerEntity(org.bukkit.World world)
	    {
	        super(((CraftWorld)world).getHandle());
	        List goalB = (List)getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
	        List goalC = (List)getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
	        List targetB = (List)getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
	        List targetC = (List)getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();

	        this.goalSelector.a(0, new PathfinderGoalFloat(this));
	        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
	        save = true;
	    }
	 
	 
	 @Override
	    public String t(){
	        return "";
	    }
	 
	  @Override
	  public void b(NBTTagCompound nbttagcompound) {
	  super.b(nbttagcompound);
	  nbttagcompound.setBoolean("save", true);
	  }
	   
	
	  @Override
	  public void a(NBTTagCompound nbttagcompound) {
	  super.a(nbttagcompound);
	  save = nbttagcompound.getBoolean("extended");
	  }
	   
	    @Override
	    public void move(double d0, double d1, double d2) {
	        return;
	    }
	 
	    @Override
	    public void g(double x, double y, double z) {
	        Vector vector = this.getBukkitEntity().getVelocity();
	        super.g(vector.getX(), vector.getY(), vector.getZ());
	    }
     @Override
     public boolean damageEntity(DamageSource damagesource, float f) {
         return false;
     }  
	 
	    public Object getPrivateField(String fieldName, Class clazz, Object object)
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
 