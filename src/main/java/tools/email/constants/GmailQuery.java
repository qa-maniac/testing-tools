package tools.email.constants;


public enum GmailQuery {
    AFTER           ("after:"),
    ANYWHERE        ("anywhere"),
    AROUND          (" AROUND "),
    ATTACHMENT      ("attachment"),
    BCC             ("bcc:"),
    BEFORE          ("before:"), // best date format: YYYY/MM/DD
    CATEGORY        ("category:"),
    CC              ("cc:"),
    DOCUMENT        ("document"),
    FILE_NAME       ("filename:"),
    FROM            ("from:"),
    HAS             ("has:"),
    IN              ("in:"),
    IS              ("is:"),
    LABEL           ("label:"),
    LARGER          ("larger:"),
    LAST            (" "),
    LIST            ("list:"),
    NEWER           ("newer:"),
    OLDER           ("older:"),
    PRESENTATION    ("presentation"),
    READ            ("read"),
    SIZE            ("size:"),
    SMALLER         ("smaller:"),
    SNOOZED         ("snoozed"),
    SPAM            ("Spam"),
    SPREADSHEET     ("spreadsheet"),
    STARRED         ("starred"),
    SUBJECT         ("subject:"),
    TO              ("to:"),
    TRASH           ("Trash"),
    UNREAD          ("unread"),
    UNSTARRED       ("unstarred"),
    YOUTUBE         ("youtube"),
    ;
    public final String value;

    GmailQuery(String value) {
        this.value = value;
    }
}