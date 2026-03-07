# Data Science Features Implementation TODO

## Database Changes
- [ ] Create Feedback entity with id, user_id, order_id, rating, comment, created_at
- [ ] Update Order entity to include feedback relationship (optional)

## Backend Updates (Java Spring Boot)
- [ ] Create Feedback.java entity class
- [ ] Create FeedbackRepository.java interface
- [ ] Create FeedbackService.java service class
- [ ] Create FeedbackController.java REST controller
- [ ] Create AnalyticsController.java to call Python service endpoints
- [ ] Update pom.xml to add RestTemplate dependency if needed

## Python Analytics Service
- [ ] Create analytics-service/ directory
- [ ] Create Flask app.py with endpoints:
  - [ ] /forecast/sales - Sales forecasting (linear regression)
  - [ ] /predict/peak-hours - Peak hour prediction (time series)
  - [ ] /analytics/menu-performance - Item popularity analysis
  - [ ] /recommendations - Apriori recommendations
  - [ ] /sentiment/analyze - Sentiment analysis (NLTK)
- [ ] Create requirements.txt for Python dependencies
- [ ] Create database connection script for MySQL access

## Frontend Updates (React)
- [ ] Create AdminAnalytics.js page with Chart.js
- [ ] Add routing for /admin/analytics in App.js
- [ ] Implement visualizations for all 5 features:
  - [ ] Sales forecasting chart
  - [ ] Peak hours heatmap
  - [ ] Menu performance charts
  - [ ] Recommendations display
  - [ ] Sentiment analysis dashboard

## Integration & Testing
- [ ] Configure Python service to run on port 5000
- [ ] Test Spring Boot to Python REST API calls
- [ ] Add sample feedback data for testing
- [ ] Test all analytics endpoints
- [ ] Update admin navigation to include analytics dashboard

## Setup & Installation
- [ ] Install Python 3.8+ and required libraries
- [ ] Run both services simultaneously (Spring Boot 9090, Flask 5000)
- [ ] Verify CORS configuration for cross-origin requests
