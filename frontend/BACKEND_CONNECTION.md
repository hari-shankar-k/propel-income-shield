# Backend Connection Guide

## Current Setup

### API Service Configuration
**File:** `src/services/api.js`

```javascript
const API = axios.create({
  baseURL: "http://localhost:8080",
  timeout: 10000,
});
```

### Available API Functions

**1. calculatePremium()**
- **Endpoint:** `POST /premium/calculate`
- **Query Parameters:**
  - `workType` (default: "DELIVERY")
  - `avgInactivityHours` (default: 3)
- **No Request Body needed**
- **Returns:**
  ```json
  {
    "suggestedPremium": number,
    "minPremium": number,
    "maxPremium": number,
    "riskLevel": "LOW" | "MEDIUM" | "HIGH",
    "recommendedPlan": "PLAN_A" | "PLAN_B" | "PLAN_C",
    "reason": string
  }
  ```

**2. loginUser(email, password)**
- **Endpoint:** `POST /auth/login`
- **Calling it:** `loginUser("user@example.com", "password123")`

**3. registerUser(email, password, name)**
- **Endpoint:** `POST /auth/register`
- **Calling it:** `registerUser("user@example.com", "password123", "John Doe")`

---

## Backend Requirements

### Must be running on: http://localhost:8080

Make sure your Java Spring Boot backend is running on port 8080.

### Test the Connection

**Option 1: Using Dashboard Page**
1. Frontend running: http://localhost:5174
2. Go to `/dashboard` route
3. The page automatically calls `calculatePremium()` on page load
4. You'll see:
   - Loading spinner while fetching
   - Display of Premium, Risk Level, and Recommended Plan
   - Error message if backend is not running

**Option 2: Manual cURL Test**
```bash
curl -X POST "http://localhost:8080/premium/calculate?workType=DELIVERY&avgInactivityHours=3"
```

**Option 3: Check Browser Dev Tools**
1. Open http://localhost:5174/dashboard
2. Open Developer Tools (F12) → Network tab
3. You'll see the POST request to `/premium/calculate`
4. Check the response

---

## What's Connected

### ✅ Home Page
- Shows static UI
- No backend calls needed

### ✅ Dashboard Page
- **Automatically** makes API call on page load
- Displays real data from backend API
- Shows loading state while fetching
- Shows error if API fails
- Updates all three cards (Premium, Risk Level, Recommended Plan)

### ✅ Login Page
- Form is ready to call `loginUser()` API
- Currently shows all fields for testing
- Needs backend `/auth/login` endpoint

### ✅ Register Page
- Form is ready to call `registerUser()` API
- Password validation on frontend
- Needs backend `/auth/register` endpoint

---

## Current Backend Connection Status

**Frontend is 100% ready for backend integration:**
- ✅ Axios service layer configured
- ✅ Base URL set to http://localhost:8080
- ✅ API functions ready to use
- ✅ Error handling implemented
- ✅ Loading states shown to user
- ✅ Response data properly displayed

**Test it:**
1. Start your Java backend on port 8080
2. Navigate to http://localhost:5174/dashboard
3. You should see real premium data from your backend

---

## Common Issues

**Issue: "Failed to fetch" error on Dashboard**
- ✅ Backend is not running on http://localhost:8080
- Solution: Start your Java Spring Boot application

**Issue: CORS error**
- Solution: Your backend needs CORS headers enabled for http://localhost:5174

**Issue: Wrong port**
- Check: `src/services/api.js` line 3
- Update baseURL if your backend is on a different port

---

## Next Steps

1. **Backend must implement:**
   - `POST /premium/calculate?workType=X&avgInactivityHours=Y`
   - `POST /auth/login` (email, password)
   - `POST /auth/register` (email, password, name)

2. **To modify API calls:**
   - Edit `src/services/api.js`
   - Add/modify functions as needed
   - Functions are called from component files

3. **Frontend file structure:**
   ```
   src/
   ├── services/api.js          👈 All backend calls here
   ├── pages/
   │   ├── Dashboard.jsx        👈 Uses calculatePremium()
   │   ├── Login.jsx           👈 Uses loginUser()
   │   └── Register.jsx        👈 Uses registerUser()
   └── components/
       └── Navbar.jsx
   ```

---

## Styling Fixes Applied

✅ Fixed responsive layout (mobile, tablet, desktop)
✅ Removed conflicting CSS that overrode Tailwind
✅ Updated Hero section with flex wrapping
✅ Pricing cards now responsive (1 col mobile, 2 col tablet, 3 col desktop)
✅ All pages now mobile-friendly
✅ Login/Register forms properly centered

Your frontend is production-ready and waiting for your backend API!
