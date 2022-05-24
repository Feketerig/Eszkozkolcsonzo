package utils.validators


fun String.isValidNameHU(): Boolean {
    return this.all { c -> c.isLetter() || c in "áÁéÉíÍóÓöÖőŐúÚüÜűŰ .-" }
}

fun String.isValidPhoneNumber(): Boolean {
    // Original string: ^\s*(?:\+?(\d{1,3}))?[-. (]*(\d{1,3})[-./ )]*(\d{3})[-. ]*(\d{3,4})\s*$
    return this.matches(Regex("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{1,3})[-./ )]*(\\d{3})[-. ]*(\\d{3,4})\\s*\$"))
}

fun String.isValidEmail(): Boolean {
    //Is stole this one from stackoverflow, dont @ me (https://stackoverflow.com/questions/201323/)
    return this.matches(Regex("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"))
}