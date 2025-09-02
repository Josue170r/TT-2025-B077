import state from "./state";
import * as actions from "./actions";
import mutations from "./mutations";
import * as getters from "./getters";

const namespaced = true;

export default {
    namespaced,
    state,
    mutations,
    actions,
    getters,
}