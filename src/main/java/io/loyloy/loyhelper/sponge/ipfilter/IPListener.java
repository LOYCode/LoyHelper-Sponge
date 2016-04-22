package io.loyloy.loyhelper.sponge.ipfilter;

import com.google.common.reflect.TypeToken;
import io.loyloy.loyhelper.sponge.LoyHelper;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class IPListener
{
    private LoyHelper plugin;

    public IPListener( LoyHelper plugin )
    {
        this.plugin = plugin;
    }

    @Listener
    public void onLogin( ClientConnectionEvent.Login event )
    {
        final String playerIP = event.getConnection().getAddress().getAddress().getHostAddress();

        if( playerIP.equals( LoyHelper.getHubIp() ) )
        {
            return;
        }

        try
        {
            if ( LoyHelper.getConfig().getNode( "ip_whitelist" ).getList( TypeToken.of( String.class ) ).contains( playerIP ) )
            {
                return;
            }
        }
        catch ( Exception e )
        {}

        event.setMessage( Text.of( TextColors.RED, "Please connect to " ).concat( LoyHelper.getHubDomain() ).concat( Text.of( " instead!" ) ) );
        event.setCancelled( true );
    }
}
