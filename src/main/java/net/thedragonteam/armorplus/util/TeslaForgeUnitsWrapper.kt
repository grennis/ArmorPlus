/*
 * Copyright (c) TheDragonTeam 2016-2017.
 */

package net.thedragonteam.armorplus.util

import net.darkhax.tesla.api.ITeslaConsumer
import net.darkhax.tesla.api.ITeslaHolder
import net.darkhax.tesla.api.ITeslaProducer
import net.minecraftforge.energy.IEnergyStorage

class TeslaForgeUnitsWrapper(private val storage: IEnergyStorage) : ITeslaProducer, ITeslaHolder, ITeslaConsumer {

    override fun givePower(power: Long, simulated: Boolean): Long = this.storage.receiveEnergy(power.toInt(), simulated).toLong()

    override fun getStoredPower(): Long = this.storage.energyStored.toLong()

    override fun getCapacity(): Long = this.storage.maxEnergyStored.toLong()

    override fun takePower(power: Long, simulated: Boolean): Long = this.storage.extractEnergy(power.toInt(), simulated).toLong()
}