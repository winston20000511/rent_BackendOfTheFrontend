var map;
const apiUrl='http://localhost:8080/api/map'
const search = document.getElementById('origin');
const iconButton = document.querySelector('.fa-solid');


search.addEventListener('keydown',(event)=>{
    if (event.key === 'Enter'){
        mapFetch();
    }
})
iconButton.addEventListener('click',()=>{
    mapFetch();
})


async function mapFetch(){
	const origin = document.getElementById('origin').value;
	const inputData = {
		origin: origin
	};
	
	try{
		const response = await fetch(apiUrl,{
			method: "POST",
			headers:{'Content-Type': 'application/json'},
			body: JSON.stringify(inputData)
		});
		
		if (!response.ok){
			throw new Error('Neetwork response was not ok')
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
    var origin = document.getElementById('origin').value;
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