# TODO: Add Cancel Order Button to Frontend

## Steps to Complete
- [x] Update OrderTracking.js to add a "Cancel Order" button that appears only if the order is cancellable (within 5 minutes, status PENDING or PREPARING).
- [x] Add logic to fetch the latest order data and check cancellation eligibility.
- [x] Implement handleCancel function to call the API and update order status in state.
- [x] Update App.js to pass orders and setOrders props to OrderTracking component.
- [x] Test the cancel button: Place order, go to tracking, cancel within 5 mins, verify UI and backend.
