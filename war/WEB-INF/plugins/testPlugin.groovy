import groovyx.gaelyk.logging.GroovyLogger
import groovyx.gaelyk.spock.plugins.TestCategory
/**
 * Test plugin.
 *
 * User: dgaviola
 * Date: 12/5/11
 * Time: 11:40 PM
 */

def log = new GroovyLogger("testPlugin")
log.info "Registering test plugin..."

binding {
    // Plugin library variables
    plugins = [
        test: [
            version: "0.1",
        ]
    ]
}

// Add test category
categories TestCategory