var map;
const mapUrl='http://localhost:8080/api/map'
const keywordUrl='http://localhost:8080/api/keyword'
const searchList = document.getElementById('searchList')
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
    addMarkerByAddress();
})


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
		//console.log('Data received:' , data);
		updateKeyWordList(data);

	}catch (error){
		console.error('There has been a problem with your fetch operation', error);
	}
}

async function updateKeyWordList(data){
	
	//const keywords=["apple", "banana", "cherry", "date", "elderberry", "fig", "grape", "kiwi", "lemon"];
	searchList.innerHTML = '';
	data.forEach(k=>{
		const li = document.createElement('li');
		li.textContent=k.city + k.township + k.street;
		li.classList.add('px-4', 'py-2', 'cursor-pointer', 'hover:bg-blue-500', 'hover:text-white');
		
		li.addEventListener('click',()=>{
			search.value=k.city + k.township + k.street;
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
	}catch (error){
		console.error('There has been a problem with your fetch operation', error);
	}

}




//Google Map
function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        center: { lat: 23.023535, lng: 120.222776 }, // 台灣的中心點 緯度 經度
        zoom: 16,
        mapId: "DEMO_MAP_ID",
    });
}

async function addMarkerByAddress() {
    var origin = document.getElementById('search').value;
    var originPosition= await geocodeAddress(origin);
    var originMaker = new google.maps.Marker({
        map: map,
        position: originPosition,
        title: origin,
    });

    // // 將地圖中心移動到新標記位置
    map.setCenter(originPosition);
    map.setZoom(16);  // 可以調整地圖縮放級別

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