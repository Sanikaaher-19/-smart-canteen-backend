# TODO: Implement Order Cancellation Functionality

## Steps to Complete
- [x] Add `cancelOrder` method in `OrderService.java` to handle cancellation logic (check 5-minute window, update status to "CANCELLED", log notification for kitchen staff).
- [x] Add new endpoint in `OrderController.java` (e.g., `PUT /api/orders/{id}/cancel`) to allow users to cancel orders, ensuring only the order owner can cancel and integrating with the service method.
- [x] Ensure existing code flow is maintained (no changes to other methods or endpoints).
- [x] Test the implementation: Place an order, attempt cancellation within 5 minutes (should succeed), and after 5 minutes (should fail). Verify logs for kitchen notification.
