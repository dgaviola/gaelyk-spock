package groovyx.gaelyk.spock.plugins

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TestCategory {
	static void callSetHeader(HttpServletResponse response) {
		response.setHeader "header1", "val1"
	}
}
