export const Constant = {
    EVENT_METHOD: {
        GET_ALL_EVENTS: 'events',
        GET_EVENTS_BY_VENDOR: (vendorID: number) => `events/vendor/${vendorID}`,
        UPDATE_EVENT: 'events/update', // PUT request
        DELETE_EVENT: (eventID: number) => `events/delete/${eventID}`, // Should append the ID to delete
        ADD_EVENT: 'events/create'
    },
    TICKETPOOL_METHOD: {
        GET_ALL_TICKETPOOLS: (eventID: number) => `ticket-pool/${eventID}`,
        SELL_TICKET: (poolID: number, vendorID: number) => `ticket-pool/${poolID}/sell-ticket/${vendorID}`,
        CREATE_POOL: (eventID: number) => `ticket-pool/create/${eventID}`
    },
    VENDOR_METHOD: {
        LOGIN_VENDOR: 'vendors/login'
    }
}