package com.desiremc.crates.data;

import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import com.desiremc.crates.DesireCrates;

public class CrateMetadata implements MetadataValue
{

    private Crate crate;

    public CrateMetadata(Crate crate)
    {
        this.crate = crate;
    }

    @Override
    public Object value()
    {
        return crate.getStub();
    }

    @Override
    public int asInt()
    {
        return crate.getId();
    }

    @Override
    public float asFloat()
    {
        return 0;
    }

    @Override
    public double asDouble()
    {
        return 0;
    }

    @Override
    public long asLong()
    {
        return 0;
    }

    @Override
    public short asShort()
    {
        return 0;
    }

    @Override
    public byte asByte()
    {
        return 0;
    }

    @Override
    public boolean asBoolean()
    {
        return false;
    }

    @Override
    public String asString()
    {
        return value().toString();
    }

    @Override
    public Plugin getOwningPlugin()
    {
        return DesireCrates.getInstance();
    }

    @Override
    public void invalidate()
    {
        
    }

}
