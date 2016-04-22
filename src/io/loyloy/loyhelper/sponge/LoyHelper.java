package io.loyloy.loyhelper.sponge;

import com.google.inject.Inject;
import io.loyloy.loyhelper.sponge.ipfilter.IPListener;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;


@Plugin(id = "loyhelper", name = "LoyHelper", version = "1.0")
public class LoyHelper
{
    private static ConfigurationNode config;

    private static final Text PREFIX = Text.builder( "[Loy]" ).color( TextColors.YELLOW ).append( Text.builder( " " ).color( TextColors.GREEN ).build() ).build();
    private static final Text HUB_DOMAIN = Text.of( "play.loyloy.io" );
    private static final String HUB_IP = "51.255.165.170";

    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private File configFile;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    public LoyHelper() {}

    @Listener
    public void onGameInitialization( GameInitializationEvent event )
    {
        setupConfig();
    }

    private void setupConfig()
    {
        try
        {
            if ( !configFile.exists() )
            {
                configFile.createNewFile();
                config = configManager.load();
                configManager.save(config);
            }

            config = configManager.load();

            if( config.getNode( "ip_filter" ).getValue() ==  null)
            {
                config.getNode( "ip_filter" ).setValue( true );
                configManager.save(config);
                config = configManager.load();
            }

            if( config.getNode( "ip_whitelist" ).getValue() ==  null)
            {
                config.getNode( "ip_whitelist" ).setValue( Arrays.asList( "127.0.0.1" ) );
                configManager.save( config );
                config = configManager.load();
            }
        }
        catch (IOException exception)
        {
            getLogger().error("There was an error in the config!");
        }
    }

    @Listener
    public void onServerStart( GameStartedServerEvent event )
    {
        if( config.getNode( "ip_filter" ).getBoolean() )
        {
            Sponge.getEventManager().registerListeners( this, new IPListener( this ) );
        }
    }

    public static String getHubIp()
    {
        return HUB_IP;
    }

    public static Text getHubDomain()
    {
        return HUB_DOMAIN;
    }

    public static Text getPfx()
    {
        return PREFIX;
    }

    public Logger getLogger()
    {
        return logger;
    }

    public static ConfigurationNode getConfig()
    {
        return config;
    }
}