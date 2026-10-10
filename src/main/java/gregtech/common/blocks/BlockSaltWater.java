package gregtech.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSaltWater extends BlockFluidClassic {

    public BlockSaltWater(Fluid fluid) {
        super(fluid, Material.water);
        setBlockName("gt.blockfluid.saltwater");
        setHardness(100.0F);
        setLightOpacity(Blocks.water.getLightOpacity());
        setTickRate(5);
    }

    @Override
    public String getLocalizedName() {
        return getFluid().getLocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {}

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return Blocks.water.getIcon(side, meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return Blocks.water.colorMultiplier(world, x, y, z);
    }

    @Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
        return !world.getBlock(x, y, z)
            .getMaterial()
            .isLiquid() && super.canDisplace(world, x, y, z);
    }

    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z) {
        return !world.getBlock(x, y, z)
            .getMaterial()
            .isLiquid() && super.displaceIfPossible(world, x, y, z);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (world.isAirBlock(x, y + 1, z)) {
            entity.motionY = Math.min(1.8D, entity.motionY + 0.1D);
        } else {
            entity.motionY = Math.min(0.7D, entity.motionY + 0.06D);
        }
        entity.fallDistance = 0.0F;
    }
}
