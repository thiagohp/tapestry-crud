/* Copied from https://issues.apache.org/jira/browse/TAP5-569. Thanks Fernando Padilla! */

Tapestry.activateZone = function ( zoneId, url ) { 
        var zoneManager = Tapestry.findZoneManagerByZoneId( zoneId ); 
        if ( zoneManager != null ) { 
                zoneManager.updateFromURL( url ); 
        } 
}; 

Tapestry.findZoneManagerByZoneId = function( zoneId ) { 
        var zoneElement = $(zoneId); 
        if (!zoneElement) { 
                Tapestry.warn("Unable to locate Ajax Zone '#{id}' for dynamic update."); 
                return null; 
        } 
        var manager = $T(zoneElement).zoneManager; 
        if (!manager) { 
                Tapestry.warn("Ajax Zone '#{id}' does not have an associated Tapestry.ZoneManager object."); 
                return null; 
        } 
        return manager; 
};