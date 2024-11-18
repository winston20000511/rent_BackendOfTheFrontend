
var map;
var markers=[];
const mapUrl='http://localhost:8080/api/map'
const keywordUrl='http://localhost:8080/api/keyword'


export function googleSearchEventConfig(){

	const search = document.getElementById('search');
	const iconButton = document.querySelector('.fa-solid');
	let isComposing = false; // 標記是否處於輸入拼音或注音過程中
	
	
	// 當輸入開始時（例如拼音輸入的過程中），設定 isComposing 為 true
	search.addEventListener('compositionstart', () => {
		isComposing = true;
	});
	
	// 當輸入結束時（輸入完成並轉換為中文字後），設定 isComposing 為 false
	search.addEventListener('compositionend', () => {
		isComposing = false;
		showKeyWordFetch()
	});
	
	//輸入時顯示List
	search.addEventListener('input',()=>{
		if (!isComposing){
			showKeyWordFetch()
		}
	
	})
	//點擊其他地方關閉searchList
	document.addEventListener('click', (e) => {
		if (!searchList.contains(e.target) && e.target !== search) {
			searchList.innerHTML = '';
			searchList.style.display = 'none';
		}
	});
	
	search.addEventListener('keydown',(event)=>{
		if (event.key === 'Enter'){
			addMarkerByAddress();
		}
	})
	
	iconButton.addEventListener('click',()=>{
		showMapFetch();
		//addMarkerByAddress();
	})

}

//Google Map
export function initMap() {
	map = new google.maps.Map(document.getElementById('map'), {
		center: { lat: 23.023535, lng: 120.222776 }, // 台灣的中心點 緯度 經度
		zoom: 12,
		mapId: "DEMO_MAP_ID",
	});
}

async function showKeyWordFetch(){
	
	const searchValue = document.getElementById('search').value;
	
	try{
		const response = await fetch(keywordUrl,{
			method: "POST",
			headers:{'Content-Type': 'text/plain'},
			body: searchValue
		});
		
		if (!response.ok){
			throw new Error('Network response was not ok')
		}
		
		const data = await response.json();
		console.log('Data received:' , data);
		updateKeyWordList(data);

	}catch (error){
		console.error('There has been a problem with your fetch operation', error);
	}
}

async function updateKeyWordList(data){
	
	//const keywords=["apple", "banana", "cherry", "date", "elderberry", "fig", "grape", "kiwi", "lemon"];
	const searchList = document.getElementById('searchList')
	searchList.innerHTML = '';
	data.forEach(k=>{
		const li = document.createElement('li');
		if (!k.city){ k.address=""};

		li.textContent=k.address;
		li.classList.add('px-4', 'py-2', 'cursor-pointer', 'hover:bg-blue-500', 'hover:text-white');

		li.addEventListener('click',()=>{
			search.value=k.adress;
			searchList.innerHTML='';
		})
		searchList.appendChild(li);
	})
	
	searchList.style.display = 'block';
	searchList.style.width = window.getComputedStyle(search).width;
	searchList.style.top = `${search.getBoundingClientRect().top + search.getBoundingClientRect().height}px`;
	searchList.style.left = `${search.getBoundingClientRect().left}px`;


}

async function showMapFetch(){
	const search = document.getElementById('search').value;
	const inputData = {
		origin: search
	};
	
	try{
		const response = await fetch(mapUrl,{
			method: "POST",
			headers:{'Content-Type': 'application/json'},
			body: JSON.stringify(inputData)
		});
		
		if (!response.ok){
			throw new Error('Network response was not ok')
		}
		
		const data = await response.json();
		console.log('Data received:' , data)
		addMarkerByAddress(data)
	}catch (error){
		console.error('There has been a problem with your fetch operation', error);
	}

}

async function addMarkerByAddress(data) {
	
	const cardContainer = document.getElementById('card-container');
	const removeCard = cardContainer.querySelectorAll('div');
	removeCard.forEach(div => div.remove());

	markers.forEach(marker => {
        marker.map = null;
    });
    markers = []; // 清空標記陣列



	var infoWindow = new google.maps.InfoWindow();
	var cardCount =1;
	//新增標記
	data.forEach(k=>{
		let beachFlagImg = document.createElement('img');
		beachFlagImg.src= "/img/house.png"
		var pos = k.city + k.township + k.street;
		var latlng = new google.maps.LatLng(k.lat, k.lng);;
		var marker = new google.maps.marker.AdvancedMarkerElement({
			position: latlng,
			map: map,
			title: k.street,
			content: beachFlagImg
		});

		//新增卡片
		const card = document.createElement('div');
		card.classList.add('bg-white', 'rounded-lg', 'shadow-md', 'p-3', 'hover:shadow-lg', 'transition-shadow', 'duration-200');
		card.id = `card${cardCount}` 
		card.innerHTML = `
		  <img src="./img/view1.jpg" alt="Card image" class="w-full h-48 object-cover mb-4 rounded-md">
		  <h3 class="text-lg font-semibold mb-2">Price ${k.city}</h3>
		  <p class="text-gray-600 mb-4">${k.city+k.township+k.street}</p>
		  <button class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">View</button>
		`;
		cardContainer.appendChild(card);
		cardCount++;

		marker.addListener('click',()=>{
			//map.setZoom(17);
			map.panTo(marker.position);
			cardSelected(card.id);
			infoWindow.setContent('<h3>' + marker.title + '</h3>');
			// infoWindow.setContent('<h3>' + marker.title + '</h3><p>' +`<strong>Distance</strong> ${distancematrix.distance.text}`+ '<p><p>' + `<strong>Duration</strong> ${distancematrix.duration.text}` + '</p>')
			infoWindow.open(map, marker)
		});

		markers.push(marker);
		// map.setCenter(latlng);
    	// map.setZoom(16);

	})
    // var origin = document.getElementById('search').value;
    // var originPosition= await geocodeAddress(origin);
    // var originMaker = new google.maps.Marker({
    //     map: map,
    //     position: originPosition,
    //     title: origin,
    // });

	let mFlagImg = document.createElement('img');
	mFlagImg.src= "/img/mhouse.png"
    // // 將地圖中心移動到新標記位置
	const address = document.getElementById('search').value;
	const origin = await geocodeAddress(address)
    const originMaker = new google.maps.marker.AdvancedMarkerElement({
        position: origin,
		map: map,
        title: address,
		content: mFlagImg
    });
	markers.push(originMaker);
	map.panTo(origin);
    //map.setCenter(origin);
    map.setZoom(15);  // 可以調整地圖縮放級別
	
	//loadMoreCards(data);

}

//取得經緯度
function geocodeAddress(address){
    return new Promise((resolve,reject) => {
        var geocoder = new google.maps.Geocoder();
        geocoder.geocode({'address':address},function(result,status){
            if (status==='OK') {
                const position = result[0].geometry.location;
                resolve(position);
            }else{
                reject('Geocode was not successful for the following reason: ' + status);
            }
        })
    });
}

//取得選擇的卡片
function cardSelected(cardId){

	document.querySelectorAll('.bg-gray-300').forEach(item =>{
		item.classList.remove('bg-gray-300')
		item.classList.add('bg-white');
	})

	const cardList = document.getElementById(cardId);
	cardList.classList.remove('bg-white');
	cardList.classList.add('bg-gray-300');
	cardList.scrollIntoView({ behavior: 'smooth', block: 'start' });
}

