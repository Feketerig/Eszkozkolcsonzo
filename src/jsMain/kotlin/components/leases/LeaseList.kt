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

val LeaseList = FC<Props> {
    var leaseList by useState(emptyList<Lease>())

    useEffectOnce {
        scope.launch {
            try {
                leaseList = getActiveLeaseList()
            } catch (e: UnauthorizedException) {
                PageNavigator.toLogin() //TODO move login redirect to somewhere more general
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