package tools.email.constants;

public enum GmailHeader {
    ARC_SEAL	                ("ARC-Seal"),
    ARC_MESSAGE_SIGNATURE	    ("ARC-Message-Signature"),
    ARC_AUTHENTICATION_RESULTS	("ARC-Authentication-Results"),
    AUTHENTICATION_RESULTS	    ("Authentication-Results"),
    CONTENT_TRANSFER_ENCODING	("Content-Transfer-Encoding"),
    CONTENT_TYPE	            ("Content-Type"),
    DATE	                    ("Date"),
    DELIVERED_TO	            ("Delivered-To"),
    DKIM_SIGNATURE	            ("DKIM-Signature"),
    FROM	                    ("From"),
    MESSAGE_ID	                ("Message-ID"),
    MIME_VERSION	            ("MIME-Version"),
    SUBJECT	                    ("Subject"),
    TO	                        ("To"),
    RECEIVED_SPF	            ("Received-SPF"),
    RETURN_PATH	                ("Return-Path"),
    X_GOOGLE_DKIM_SIGNATURE	    ("X-Google-DKIM-Signature"),
    X_GM_MESSAGE_STATE	        ("X-Gm-Message-State"),
    X_GOOGLE_SMTP_SOURCE	    ("X-Google-Smtp-Source"),
    ;
    public final String string;

    GmailHeader(String string) {
        this.string = string;
    }
}