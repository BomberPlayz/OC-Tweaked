package li.cil.oc.server.component

import java.util

import li.cil.oc.Constants
import li.cil.oc.api.driver.DeviceInfo.DeviceAttribute
import li.cil.oc.api.driver.DeviceInfo.DeviceClass
import li.cil.oc.Settings
import li.cil.oc.api.Network
import li.cil.oc.api.driver.DeviceInfo
import li.cil.oc.api.network.EnvironmentHost
import li.cil.oc.api.network.Visibility
import li.cil.oc.api.prefab
import li.cil.oc.api.prefab.network.{AbstractManagedEnvironment, AbstractManagedNodeHost}
import li.cil.oc.util.BlockPosition
import net.minecraft.util.EnumFacing

import scala.collection.convert.WrapAsJava._

class UpgradeSolarGenerator(val host: EnvironmentHost) extends AbstractManagedEnvironment with DeviceInfo {
  override val getNode = Network.newNode(this, Visibility.NETWORK).
    withConnector().
    create()

  var ticksUntilCheck = 0

  var isSunShining = false

  private final lazy val deviceInfo = Map(
    DeviceAttribute.Class -> DeviceClass.Power,
    DeviceAttribute.Description -> "Solar panel",
    DeviceAttribute.Vendor -> Constants.DeviceInfo.DefaultVendor,
    DeviceAttribute.Product -> "Enligh10"
  )

  override def getDeviceInfo: util.Map[String, String] = deviceInfo

  // ----------------------------------------------------------------------- //

  override val canUpdate = true

  override def update() {
    super.update()

    ticksUntilCheck -= 1
    if (ticksUntilCheck <= 0) {
      ticksUntilCheck = 100
      isSunShining = isSunVisible
    }
    if (isSunShining) {
      getNode.changeBuffer(Settings.get.solarGeneratorEfficiency)
    }
  }

  private def isSunVisible = {
    val blockPos = BlockPosition(host).offset(EnumFacing.UP)
    host.getWorld.isDaytime &&
      (!host.getWorld.provider.hasNoSky) &&
      host.getWorld.canBlockSeeSky(blockPos.toBlockPos) &&
      (!host.getWorld.getBiome(blockPos.toBlockPos).canRain || (!host.getWorld.isRaining && !host.getWorld.isThundering))
  }
}
