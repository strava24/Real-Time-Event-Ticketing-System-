export const Constant = {
    EVENT_METHOD: {
        GET_ALL_EVENTS: 'events',
        GET_EVENTS_BY_VENDOR: (vendorID: number) => `events/vendor/${vendorID}`,
        UPDATE_EVENT: 'events/update', // PUT request
        DELETE_EVENT: (eventID: number) => `events/delete/${eventID}`, // Should append the ID to delete
        ADD_EVENT: 'events/create',
        ADD_EVENT_IMAGE: 'events/create/image',
        GET_EVENT_IMAGE: (eventID: number) => `events/${eventID}/image`,
        UPDATE_EVENT_IMAGE: 'events/update/image'
    },
    TICKETPOOL_METHOD: {
        GET_ALL_TICKETPOOLS: (eventID: number) => `ticket-pool/${eventID}`,
        SELL_TICKET: (poolID: number, vendorID: number) => `ticket-pool/${poolID}/sell-ticket/${vendorID}`,
        CREATE_POOL: (eventID: number) => `ticket-pool/create/${eventID}`,
        BUY_TICKET: (poolID: number, customerID: number) => `ticket-pool/${poolID}/buy-ticket/${customerID}`
    },
    VENDOR_METHOD: {
        LOGIN_VENDOR: 'vendors/login',
        SIGNUP_VENDOR: 'vendors/signup',
        GET_VENDOR: (vendorID: number) => `vendors/${vendorID}`,
        UPDATE_VENDOR: (vendorID: number) => `vendors/${vendorID}`, // put request
        GET_EVENT_COUNT: (vendorID: number) => `vendors/events/${vendorID}`
    },
    CUSTOMER_METHOD: {
        LOGIN_CUSTOMER: 'customers/login',
        SIGNUP_CUSTOMER: 'customers/signup',
        GET_CUSTOMER: (customerID: number) => `customers/${customerID}`,
        UPDATE_CUSTOMER: (customerID: number) => `customers/${customerID}` // put request
    }
}