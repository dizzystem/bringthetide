package dizzystem.bringthetide.block.properties;

import net.minecraft.util.StringRepresentable;

public enum PillarDirection implements StringRepresentable {
    TOP("top"),
    MIDDLE("middle"),
    BASE("base");

    private final String name;

    private PillarDirection(String name) {
        this.name = name;
    }

    public String toString() {
        return this.getSerializedName();
    }

    public String getSerializedName() {
        return this.name;
    }
}