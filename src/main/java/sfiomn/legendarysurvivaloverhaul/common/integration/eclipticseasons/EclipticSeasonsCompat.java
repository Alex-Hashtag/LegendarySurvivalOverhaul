package sfiomn.legendarysurvivaloverhaul.common.integration.eclipticseasons;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Reflection-based access to Ecliptic Seasons API to avoid a hard compile-time dependency.
 * All methods return sensible defaults if the API is not present or calls fail.
 */
public final class EclipticSeasonsCompat
{
    private static final String API_CLASS = "com.teamtea.eclipticseasons.api.EclipticSeasonsApi";

    private EclipticSeasonsCompat()
    {
    }

    private static Object apiInstance() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        Class<?> api = Class.forName(API_CLASS);
        Method getInstance = api.getMethod("getInstance");
        return getInstance.invoke(null);
    }

    public static boolean isSeasonEnabled(Level level)
    {
        try
        {
            Object api = apiInstance();
            Method m = api.getClass().getMethod("isSeasonEnabled", Level.class);
            Object res = m.invoke(api, level);
            return res instanceof Boolean b && b;
        } catch (Throwable t)
        {
            return false;
        }
    }

    public static Object getSolarTerm(Level level)
    {
        try
        {
            Object api = apiInstance();
            Method m = api.getClass().getMethod("getSolarTerm", Level.class);
            return m.invoke(api, level);
        } catch (Throwable t)
        {
            return null;
        }
    }

    public static boolean isSolarTermNone(Level level)
    {
        Object term = getSolarTerm(level);
        if (term == null) return true;
        try
        {
            // Compare enum constant name to "NONE"
            Method name = term.getClass().getMethod("name");
            Object n = name.invoke(term);
            return "NONE".equals(String.valueOf(n));
        } catch (Throwable t)
        {
            return true;
        }
    }

    public static int getSolarTermDayTime(Level level)
    {
        Object term = getSolarTerm(level);
        if (term == null) return 12000;
        try
        {
            Method m = term.getClass().getMethod("getDayTime");
            Object res = m.invoke(term);
            return res instanceof Number n ? n.intValue() : 12000;
        } catch (Throwable t)
        {
            return 12000;
        }
    }

    public static int getLastingDaysOfEachTerm(Level level)
    {
        try
        {
            Object api = apiInstance();
            Method m = api.getClass().getMethod("getLastingDaysOfEachTerm", Level.class);
            Object res = m.invoke(api, level);
            return res instanceof Number n ? n.intValue() : 0;
        } catch (Throwable t)
        {
            return 0;
        }
    }

    public static double getSolarDays(Level level)
    {
        try
        {
            Object api = apiInstance();
            Method m = api.getClass().getMethod("getSolarDays", Level.class);
            Object res = m.invoke(api, level);
            return res instanceof Number n ? n.doubleValue() : 0.0;
        } catch (Throwable t)
        {
            return 0.0;
        }
    }

    public static float getTimeInTerm(Level level)
    {
        try
        {
            Object api = apiInstance();
            Method m = api.getClass().getMethod("getTimeInTerm", Level.class);
            Object res = m.invoke(api, level);
            return res instanceof Number n ? n.floatValue() : 0.0f;
        } catch (Throwable t)
        {
            return 0.0f;
        }
    }

    public static Biome.Precipitation getCurrentPrecipitationAt(Level level, BlockPos pos)
    {
        try
        {
            Object api = apiInstance();
            Method m = api.getClass().getMethod("getCurrentPrecipitationAt", Level.class, BlockPos.class);
            Object res = m.invoke(api, level, pos);
            return (Biome.Precipitation) res;
        } catch (Throwable t)
        {
            return Biome.Precipitation.NONE;
        }
    }

    public static int getSolarTermOrdinal(Level level)
    {
        Object term = getSolarTerm(level);
        if (term == null) return 0;
        try
        {
            Method m = term.getClass().getMethod("ordinal");
            Object res = m.invoke(term);
            return res instanceof Number n ? n.intValue() : 0;
        } catch (Throwable t)
        {
            return 0;
        }
    }

    public static Component getSolarTermTranslation(Level level)
    {
        Object term = getSolarTerm(level);
        if (term == null) return Component.empty();
        try
        {
            Method m = term.getClass().getMethod("getTranslation");
            Object res = m.invoke(term);
            return (Component) res;
        } catch (Throwable t)
        {
            return Component.empty();
        }
    }

    public static Component getSeasonTranslation(Level level)
    {
        Object term = getSolarTerm(level);
        if (term == null) return Component.empty();
        try
        {
            Method getSeason = term.getClass().getMethod("getSeason");
            Object season = getSeason.invoke(term);
            if (season == null) return Component.empty();
            Method getTranslation = season.getClass().getMethod("getTranslation");
            Object res = getTranslation.invoke(season);
            return (Component) res;
        } catch (Throwable t)
        {
            return Component.empty();
        }
    }
}
