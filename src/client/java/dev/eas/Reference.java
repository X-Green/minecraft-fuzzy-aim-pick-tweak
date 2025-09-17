package dev.eas;

import net.minecraft.DetectedVersion;

import fi.dy.masa.malilib.util.StringUtils;

public class Reference
{
    public static final String MOD_ID = "fuzzyaimtweak";
    public static final String MOD_NAME = "FuzzyAimTweak";
    public static final String MOD_VERSION = StringUtils.getModVersionString(MOD_ID);
    public static final String MC_VERSION = DetectedVersion.BUILT_IN.name();
    public static final String MOD_TYPE = "fabric";
    public static final String MOD_STRING = MOD_ID+"-"+MOD_TYPE+"-"+MC_VERSION+"-"+MOD_VERSION;
}
