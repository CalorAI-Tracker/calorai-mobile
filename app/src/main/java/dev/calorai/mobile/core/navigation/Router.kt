package dev.calorai.mobile.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import dev.calorai.mobile.core.utils.ObserveAsEvents
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow


typealias NavigationCommand = NavController.() -> Unit

interface Router {

    val destination: NavDestination?

    suspend fun emit(navigationCommand: NavigationCommand)

    fun tryEmit(navigationCommand: NavigationCommand)
}

interface RouterController : Router {
    val navigationCommands: Flow<NavigationCommand>

    fun setUpRouter(destinationProvider: () -> NavDestination?)
}

interface RouterContext

data object GlobalRouterContext : RouterContext

class RouterControllerImpl constructor() : RouterController {

    private val commandChannel: Channel<NavigationCommand> = Channel(BUFFERED)
    override val navigationCommands: Flow<NavigationCommand> = commandChannel.receiveAsFlow()

    private var destinationProvider: (() -> NavDestination?)? = null

    override fun setUpRouter(destinationProvider: () -> NavDestination?) {
        this.destinationProvider = destinationProvider
    }

    override val destination: NavDestination?
        get() = destinationProvider?.invoke()

    override suspend fun emit(navigationCommand: NavigationCommand) {
        commandChannel.send(navigationCommand)
    }

    override fun tryEmit(navigationCommand: NavigationCommand) {
        commandChannel.trySend(navigationCommand)
    }
}

@Composable
fun RouterController.setupWith(navController: NavController, context: RouterContext) {
    LaunchedEffect("setUpRouter(context=${context})") {
        setUpRouter(
            destinationProvider = { navController.currentBackStackEntry?.destination },
        )
    }
    ObserveAsEvents(navigationCommands) { command -> command(navController) }
}
