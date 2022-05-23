package components.leases

import getActiveLeaseList
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import model.Lease
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.useEffectOnce
import react.useState
import utils.browser.PageNavigator
import utils.exceptions.UnauthorizedException

private val scope = MainScope()

external interface LeaseListProps : Props

val LeaseList = FC<LeaseListProps> {
    var leaseList by useState(emptyList<Lease>())

    useEffectOnce {
        scope.launch {//TODO This is great, it works, but it shouldnt be here. redirect to login should happen on the server immediately
            //TODO This kind of solution is only for low access level, not generally having to be logged in.
            //TODO Also this should be in a function, not to spam it everywhere
            try {
                leaseList = getActiveLeaseList()
            } catch (e: UnauthorizedException) {
                PageNavigator.toLogin()
            }
        }
    }

    ReactHTML.ul {
        leaseList.forEach { item ->
            LeaseListItem {
                lease = item
            }
        }
    }
}