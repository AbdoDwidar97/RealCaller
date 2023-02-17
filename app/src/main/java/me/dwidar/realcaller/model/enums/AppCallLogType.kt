package me.dwidar.realcaller.model.enums

enum class AppCallLogType
{
    OutgoingCall {
        override fun toString(): String {
                return "Outgoing Call"
            }
                 },

    ReceivedCall {
        override fun toString(): String {
            return "Received Call"
        }
    },

    MissedCall {
        override fun toString(): String {
            return "Missed Call"
        }
    },

    OtherCall {
        override fun toString(): String {
            return "Other"
        }
    },
}