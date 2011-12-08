package groovyx.gaelyk.spock

import com.google.appengine.api.LifecycleManager
import com.google.appengine.api.NamespaceManager
import com.google.appengine.api.backends.*
import com.google.appengine.api.blobstore.*
import com.google.appengine.api.capabilities.*
import com.google.appengine.api.channel.*
import com.google.appengine.api.datastore.*
import com.google.appengine.api.files.*
import com.google.appengine.api.mail.*
import com.google.appengine.api.memcache.*
import com.google.appengine.api.oauth.*
import com.google.appengine.api.urlfetch.*
import com.google.appengine.api.users.*
import com.google.appengine.api.utils.SystemProperty
import com.google.appengine.api.taskqueue.*
import com.google.appengine.api.xmpp.*
import com.google.appengine.tools.development.testing.*
import groovyx.gaelyk.*
import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletResponse
import groovyx.gaelyk.plugins.PluginsHandler
import spock.lang.Specification

class GaelykUnitSpec extends Specification {
	
	def groovletInstance
	def helper
	def sout, out, response
	def datastore, memcache, mail, urlFetch, images, users, user
	def defaultQueue, queues, xmpp, blobstore, files, oauth, channel
	def namespace, localMode, app, capabilities, backends, lifecycle
	
	def setup(){
		//system properties to be set
		SystemProperty.environment.set("Development")
		SystemProperty.version.set("0.1")
		SystemProperty.applicationId.set("1234")
		SystemProperty.applicationVersion.set("1.0")
	
		helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig(),
			new LocalMemcacheServiceTestConfig(),
			new LocalMailServiceTestConfig(),
			new LocalImagesServiceTestConfig(),
			new LocalUserServiceTestConfig(),
			new LocalTaskQueueTestConfig(),
			new LocalXMPPServiceTestConfig(),
			new LocalBlobstoreServiceTestConfig(),
			new LocalFileServiceTestConfig()
		)
		helper.setUp()

        Object.mixin GaelykCategory
        PluginsHandler.instance.scriptContent = { String path ->
            def file = new File("war/" + path)
            file.exists() ? file.text : ""
        }
        PluginsHandler.instance.initPlugins()
        PluginsHandler.instance.categories.each {
            Object.mixin it
        }

		sout = Mock(ServletOutputStream)
		out = Mock(PrintWriter)
		response = Mock(HttpServletResponse)
		oauth = Mock(OAuthService)
		channel = Mock(ChannelService)
		urlFetch = Mock(URLFetchService)
		capabilities = Mock(CapabilitiesService)
		backends = Mock(BackendService)
		
		datastore = DatastoreServiceFactory.datastoreService
		memcache = MemcacheServiceFactory.memcacheService
		mail = MailServiceFactory.mailService
		images = ImagesServiceWrapper.instance
		users = UserServiceFactory.userService
		user = users.currentUser
		defaultQueue = QueueFactory.defaultQueue
		queues = new QueueAccessor()
		xmpp = XMPPServiceFactory.XMPPService
		blobstore = BlobstoreServiceFactory.blobstoreService
		files = FileServiceFactory.fileService
		lifecycle = LifecycleManager.instance

		namespace = NamespaceManager
		localMode = (SystemProperty.environment.value() == SystemProperty.Environment.Value.Development)
		
		app = [
			env: [
				name: SystemProperty.environment.value(),
				version: SystemProperty.version.get(),
			],
			gaelyk: [
				version: '1.1'
			],
			id: SystemProperty.applicationId.get(),
			version: SystemProperty.applicationVersion.get()
		]

	}
	
	def teardown(){
		helper.tearDown()
	}
		
	def groovlet = {
		groovletInstance = new GroovletUnderSpec("$it")
		
		[ 'sout', 'out', 'response', 'datastore', 'memcache', 'mail', 'urlFetch', 'images', 'users', 'user', 'defaultQueue', 'queues', 'xmpp', 
		  'blobstore', 'files', 'oauth', 'channel', 'capabilities', 'namespace', 'localMode', 'app', 'backends', 'lifecycle'
		].each { groovletInstance."$it" = this."$it" }
		
		this.metaClass."${it.tokenize('.').first()}" = groovletInstance
	}
		
}