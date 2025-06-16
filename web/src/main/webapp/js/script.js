// Main JavaScript for BidHub

// This would contain shared functionality across pages
// For this demo, we'll implement some core functions

/**
 * Initialize tooltips
 */
function initTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

/**
 * Format currency
 */
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(amount);
}

/**
 * Initialize countdown timers
 */
function initCountdownTimers() {
    const timers = document.querySelectorAll('.countdown-timer');

    timers.forEach(timer => {
        const endTime = new Date(timer.dataset.endTime).getTime();

        const updateTimer = () => {
            const now = new Date().getTime();
            const distance = endTime - now;

            const days = Math.floor(distance / (1000 * 60 * 60 * 24));
            const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            const seconds = Math.floor((distance % (1000 * 60)) / 1000);

            timer.textContent = `${days}d ${hours}h ${minutes}m ${seconds}s`;

            if (distance < 0) {
                timer.textContent = "Auction Ended";
                clearInterval(interval);
            }
        };

        updateTimer();
        const interval = setInterval(updateTimer, 1000);
    });
}

/**
 * Initialize auto-bid functionality
 */
function initAutoBid() {
    const autoBidToggle = document.getElementById('autoBidToggle');
    const autoBidContainer = document.getElementById('autoBidContainer');

    if (autoBidToggle && autoBidContainer) {
        autoBidToggle.addEventListener('change', function() {
            autoBidContainer.style.display = this.checked ? 'block' : 'none';
        });
    }
}

/**
 * Initialize quick bid buttons
 */
function initQuickBidButtons() {
    document.querySelectorAll('.bid-btn').forEach(button => {
        button.addEventListener('click', function() {
            const increment = parseInt(this.dataset.increment);
            const bidAmountInput = document.getElementById('bidAmount');
            const currentValue = parseInt(bidAmountInput.value) || 0;
            bidAmountInput.value = currentValue + increment;
        });
    });
}

/**
 * Initialize place bid functionality
 */
function initPlaceBid() {
    const placeBidBtn = document.getElementById('placeBidBtn');

    if (placeBidBtn) {
        placeBidBtn.addEventListener('click', function() {
            const bidAmount = parseInt(document.getElementById('bidAmount').value);
            const currentBid = parseInt(document.getElementById('currentBid').textContent);

            if (isNaN(bidAmount)) {
                alert('Please enter a valid bid amount');
                return;
            }

            if (bidAmount <= currentBid) {
                alert('Your bid must be higher than the current bid');
                return;
            }

            // In a real app, this would be an API call
            placeBid(bidAmount);
        });
    }
}

/**
 * Simulate placing a bid
 */
function placeBid(amount) {
    // Get current product ID from URL
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get('id');

    // Get existing bids from localStorage or initialize
    let bids = JSON.parse(localStorage.getItem('bids')) || {};

    // Initialize product bids if not exists
    if (!bids[productId]) {
        bids[productId] = [];
    }

    // Add new bid
    const newBid = {
        bidder: "CurrentUser", // In real app, this would be the logged in user
        amount: amount,
        time: new Date().toISOString()
    };

    bids[productId].push(newBid);

    // Save to localStorage
    localStorage.setItem('bids', JSON.stringify(bids));

    // Update UI
    updateBidUI(amount);

    // Show success message
    alert(`Bid placed successfully for ${formatCurrency(amount)}`);
}

/**
 * Update UI after placing a bid
 */
function updateBidUI(amount) {
    // Update current bid display
    document.getElementById('currentBid').textContent = amount;

    // Update bid count
    const bidCountElement = document.getElementById('bidCount');
    const currentCount = parseInt(bidCountElement.textContent) || 0;
    bidCountElement.textContent = currentCount + 1;

    // Update bid amount field minimum and value
    const bidAmountInput = document.getElementById('bidAmount');
    bidAmountInput.min = amount + 10;
    bidAmountInput.value = amount + 10;

    // Add to bidding history
    addBidToHistory("CurrentUser", amount, new Date());
}

/**
 * Add bid to history table
 */
function addBidToHistory(bidder, amount, time) {
    const bidHistoryTable = document.getElementById('bidHistory');

    if (bidHistoryTable) {
        const newRow = document.createElement('tr');
        newRow.innerHTML = `
            <td>${bidder}</td>
            <td>${formatCurrency(amount)}</td>
            <td>${time.toLocaleString()}</td>
        `;
        bidHistoryTable.insertBefore(newRow, bidHistoryTable.firstChild);
    }
}

/**
 * Load bidding history for a product
 */
function loadBiddingHistory(productId) {
    const bidHistoryTable = document.getElementById('bidHistory');

    if (bidHistoryTable) {
        // Clear existing rows
        bidHistoryTable.innerHTML = '';

        // Get bids from localStorage
        const bids = JSON.parse(localStorage.getItem('bids')) || {};
        const productBids = bids[productId] || [];

        // Add each bid to the table
        productBids.reverse().forEach(bid => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${bid.bidder}</td>
                <td>${formatCurrency(bid.amount)}</td>
                <td>${new Date(bid.time).toLocaleString()}</td>
            `;
            bidHistoryTable.appendChild(row);
        });
    }
}

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    initTooltips();
    initCountdownTimers();
    initAutoBid();
    initQuickBidButtons();
    initPlaceBid();

    // If on product page, load bidding history
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get('id');
    if (productId) {
        loadBiddingHistory(productId);
    }
});