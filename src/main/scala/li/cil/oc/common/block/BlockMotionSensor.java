package li.cil.oc.common.block;

import li.cil.oc.common.tileentity.TileEntityMotionSensor;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public final class BlockMotionSensor extends AbstractBlock {
    @Nullable
    @Override
    protected Class<? extends TileEntity> getTileEntityClass() {
        return TileEntityMotionSensor.class;
    }
}
