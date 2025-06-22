
document.addEventListener('DOMContentLoaded', function() {
    
    
    const searchInput = document.querySelector('.search-input');
    const searchBtn = document.querySelector('.search-btn');
    
 
    searchBtn.addEventListener('click', function() {
        performSearch();
    });
    
  
    searchInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            performSearch();
        }
    });
    
   
    function performSearch() {
        const query = searchInput.value.trim();
        if (query) {
            console.log('Searching for:', query);
            showSearchResults(query);
        }
    }
    
 
    function showSearchResults(query) {
        alert(`Searching for: "${query}"\nThis would show search results in a real application.`);
    }
    
 
    const dropdown = document.querySelector('.dropdown');
    const dropdownBtn = document.querySelector('.dropdown-btn');
    const dropdownContent = document.querySelector('.dropdown-content');
    
    if (dropdown && dropdownBtn && dropdownContent) {
        dropdownBtn.addEventListener('click', function(e) {
            e.preventDefault();
            dropdownContent.style.display = 
                dropdownContent.style.display === 'block' ? 'none' : 'block';
        });
        
     
        document.addEventListener('click', function(e) {
            if (!dropdown.contains(e.target)) {
                dropdownContent.style.display = 'none';
            }
        });
        
  
        const categoryItems = document.querySelectorAll('.main-category');
        categoryItems.forEach(item => {
            item.addEventListener('click', function(e) {
                e.preventDefault();
                const categoryName = this.textContent;
                console.log('Selected category:', categoryName);
                dropdownContent.style.display = 'none';
          
                const categoryText = dropdownBtn.querySelector('text') || dropdownBtn.childNodes[2];
                if (categoryText) {
                    categoryText.textContent = categoryName;
                }
            });
        });
    }
    
    
    const heartIcon = document.querySelector('.fa-heart');
    const commentIcon = document.querySelector('.fa-comment');
    
    if (heartIcon) {
        heartIcon.addEventListener('click', function() {
            this.classList.toggle('fas');
            this.classList.toggle('far');
            console.log('Wishlist toggled');
        });
    }
    
    if (commentIcon) {
        commentIcon.addEventListener('click', function() {
            console.log('Messages clicked');
            alert('Messages feature would open here');
        });
    }
    
  
    const shopBtns = document.querySelectorAll('.shop-btn, .shop-btn-small, .shop-btn-right');
    shopBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            const card = this.closest('.ad-card');
            const productTitle = card.querySelector('.product-title, .product-title-small, .product-title-right');
            const productName = productTitle ? productTitle.textContent : 'this product';
            
            console.log('Shop now clicked for:', productName);
            showProductDetails(productName);
        });
    });
    
    function showProductDetails(productName) {
        const modal = createModal('Product Details', `
            <div class="product-details">
                <h4>${productName}</h4>
                <p>Product details would be displayed here in a real application.</p>
                <div class="product-actions">
                    <button class="add-to-cart-btn">Add to Cart</button>
                    <button class="buy-now-btn">Buy Now</button>
                </div>
            </div>
        `);
        document.body.appendChild(modal);
        
       
        const addToCartBtn = modal.querySelector('.add-to-cart-btn');
        addToCartBtn.addEventListener('click', function() {
            console.log('Added to cart:', productName);
            alert(`${productName} added to cart!`);
        });
        
 
        const buyNowBtn = modal.querySelector('.buy-now-btn');
        buyNowBtn.addEventListener('click', function() {
            console.log('Buy now clicked for:', productName);
            alert(`Proceeding to checkout for ${productName}`);
        });
    }
    

    function createModal(title, content) {
        const modal = document.createElement('div');
        modal.className = 'modal-overlay';
        modal.innerHTML = `
            <div class="modal-content">
                <div class="modal-header">
                    <h3>${title}</h3>
                    <button class="close-btn">&times;</button>
                </div>
                <div class="modal-body">
                    ${content}
                </div>
            </div>
        `;
        
      
        const closeBtn = modal.querySelector('.close-btn');
        closeBtn.addEventListener('click', function() {
            document.body.removeChild(modal);
        });
        
      
        modal.addEventListener('click', function(e) {
            if (e.target === modal) {
                document.body.removeChild(modal);
            }
        });
        
        return modal;
    }
    

    const navLinks = document.querySelectorAll('a[href^="#"]');
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const targetId = this.getAttribute('href');
            const targetElement = document.querySelector(targetId);
            if (targetElement) {
                targetElement.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
    

    const phoneNumber = document.querySelector('.contact-info span');
    if (phoneNumber) {
        phoneNumber.style.cursor = 'pointer';
        phoneNumber.addEventListener('click', function() {
            window.location.href = `tel:${this.textContent}`;
        });
    }
    

    const socialLinks = document.querySelectorAll('.follow-links a');
    socialLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const icon = this.querySelector('i');
            let platform = '';
            
            if (icon.classList.contains('fa-facebook-f')) {
                platform = 'Facebook';
            } else if (icon.classList.contains('fa-instagram')) {
                platform = 'Instagram';
            } else if (icon.classList.contains('fa-twitter')) {
                platform = 'Twitter';
            }
            
            console.log(`Opening ${platform}`);
            alert(`This would open ${platform} in a real application.`);
        });
    });
    
 
    function animateOnScroll() {
        const elements = document.querySelectorAll('.ad-card, .hero-section');
        
        elements.forEach(element => {
            const elementTop = element.getBoundingClientRect().top;
            const elementVisible = 150;
            
            if (elementTop < window.innerHeight - elementVisible) {
                element.style.opacity = '1';
                element.style.transform = 'translateY(0)';
            }
        });
    }
    
  
    const animatedElements = document.querySelectorAll('.ad-card, .hero-section');
    animatedElements.forEach(element => {
        element.style.opacity = '0';
        element.style.transform = 'translateY(30px)';
        element.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
    });
    

    window.addEventListener('scroll', animateOnScroll);
    
  
    animateOnScroll();
    

    const mobileMenuToggle = document.createElement('button');
    mobileMenuToggle.className = 'mobile-menu-toggle';
    mobileMenuToggle.innerHTML = '<i class="fas fa-bars"></i>';
    mobileMenuToggle.style.display = 'none';
    

    function handleResize() {
        if (window.innerWidth <= 768) {
            const headerContent = document.querySelector('.header-content');
            if (headerContent && !headerContent.querySelector('.mobile-menu-toggle')) {
                headerContent.appendChild(mobileMenuToggle);
                mobileMenuToggle.style.display = 'block';
            }
        } else {
            mobileMenuToggle.style.display = 'none';
        }
    }
    
    window.addEventListener('resize', handleResize);
    handleResize(); 
    
    console.log('Ownify marketplace JavaScript loaded successfully!');
});


const style = document.createElement('style');
style.textContent = `
    .modal-overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.7);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 10000;
    }
    
    .modal-content {
        background: white;
        border-radius: 15px;
        padding: 0;
        max-width: 400px;
        width: 90%;
        max-height: 80vh;
        overflow-y: auto;
    }
    
    .modal-header {
        padding: 20px;
        border-bottom: 1px solid #eee;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    
    .modal-header h3 {
        margin: 0;
        color: #333;
    }
    
    .close-btn {
        background: none;
        border: none;
        font-size: 24px;
        cursor: pointer;
        color: #999;
    }
    
    .close-btn:hover {
        color: #333;
    }
    
    .modal-body {
        padding: 20px;
    }
    
    .product-details {
        text-align: center;
    }
    
    .product-details h4 {
        margin-bottom: 15px;
        color: #333;
    }
    
    .product-details p {
        margin-bottom: 20px;
        color: #666;
    }
    
    .product-actions {
        display: flex;
        gap: 10px;
    }
    
    .add-to-cart-btn,
    .buy-now-btn {
        flex: 1;
        padding: 10px;
        border: none;
        border-radius: 8px;
        cursor: pointer;
        font-weight: bold;
        transition: all 0.3s;
    }
    
    .add-to-cart-btn {
        background-color: #f8f9fa;
        color: #333;
        border: 1px solid #ddd;
    }
    
    .add-to-cart-btn:hover {
        background-color: #e9ecef;
    }
    
    .buy-now-btn {
        background-color: #ff6b35;
        color: white;
    }
    
    .buy-now-btn:hover {
        background-color: #e55a2b;
    }
    
    .mobile-menu-toggle {
        display: none;
        background: none;
        border: none;
        color: white;
        font-size: 18px;
        cursor: pointer;
    }
    
    @media (max-width: 768px) {
        .modal-content {
            margin: 20px;
            max-width: none;
        }
    }
`;

document.head.appendChild(style);
