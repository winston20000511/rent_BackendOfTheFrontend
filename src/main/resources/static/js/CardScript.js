//document.addEventListener('DOMContentLoaded', () => {
const cardContainer = document.getElementById('card-container');
const mainRight = document.getElementById('mRight')
let cardCount = 0;
<<<<<<< HEAD
  
function loadMoreCards() {
  for (let i = 0; i < 6; i++) {
    const card = document.createElement('div');
    card.classList.add('bg-white', 'rounded-lg', 'shadow-md', 'p-3', 'hover:shadow-lg', 'transition-shadow', 'duration-200');
    card.innerHTML = `
      <img src="/img/view1.jpg" alt="Card image" class="w-full h-48 object-cover mb-4 rounded-md">
      <h3 class="text-lg font-semibold mb-2">Price ${cardCount + 1}</h3>
      <p class="text-gray-600 mb-4">This is card number ${cardCount + 1}. It contains some descriptive content.</p>
=======

export function cardEventConfig() {
  document.addEventListener("DOMContentLoaded", () => {

    //loadMoreCards();
    mainRight.addEventListener("scroll", handleScroll);
  });


}

export function loadMoreCards(cardCount) {
  for (let i = 0; i < cardCount; i++) {
    const card = document.createElement("div");
    card.classList.add(
      "bg-white",
      "rounded-lg",
      "shadow-md",
      "p-3",
      "hover:shadow-lg",
      "transition-shadow",
      "duration-200"
    );
    card.innerHTML = `
      <img src="./img/view1.jpg" alt="Card image" class="w-full h-48 object-cover mb-4 rounded-md">
      <h3 class="text-lg font-semibold mb-2">Price ${i + 1}</h3>
      <p class="text-gray-600 mb-4">This is card number ${i + 1}. It contains some descriptive content.</p>
>>>>>>> origin/OK
      <button class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">View</button>
	    <button class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600" onclick="bookingPage(${i + 1})">預約</button>
	  	<button class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600" onclick="editSlot(${i + 1})">編輯時段</button>
    `;
    cardContainer.appendChild(card);
    cardCount++;
  }
}
  
function handleScroll() {
  const { scrollTop, scrollHeight, clientHeight } = mainRight;
  //console.log({ scrollTop, scrollHeight, clientHeight });

  if (scrollTop + clientHeight >= scrollHeight-5) {
    loadMoreCards();
  }
}
<<<<<<< HEAD
  
    loadMoreCards();
    //mainRight.addEventListener('scroll', handleScroll);
	
  //});
=======

function bookingPage(houseId) {
  console.log(houseId)
  window.location.href = "/booking/house/" + houseId;
}

function editSlot(houseId) {
  window.location.href = "/booking/timeSlot/" + houseId;
}


>>>>>>> origin/OK
