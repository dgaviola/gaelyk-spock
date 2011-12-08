package groovyx.gaelyk.spock

class BasicPluginCasesCovered extends GaelykUnitSpec  {

	def setup(){
		groovlet 'basicPluginsGroovlet.groovy'
	}

    def "plugin categories are accessible"() {
        when: "we call a groovlet that uses some plugin categories"
        basicPluginsGroovlet.get()

        then: "see the effects of using those categories"
        1 * response.setHeader("header1", "val1")
    }
}
