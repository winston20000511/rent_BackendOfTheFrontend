export default {
    props: {
      houseId: {
        type: [String, Number],
        required: true
      },
      baseUrl: {
        type: String,
        default: '/api/houses/getPhotos'
      }
    },
    data() {
      return {
        photos: [],
        currentPhotoIndex: 0,
        loading: true,
        error: null
      }
    },
    computed: {
      currentPhoto() {
        return this.photos[this.currentPhotoIndex]
      }
    },
    methods: {
      async fetchHousePhotos() {
        this.loading = true
        this.error = null
        
        try {
          const response = await fetch(`${this.baseAddress}/${this.houseId}`)
          
          if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`)
          }
          
          const data = await response.json()
          this.photos = data
          this.currentPhotoIndex = 0
        } catch (error) {
          console.error('Error fetching house photos:', error)
          this.error = error
          this.photos = []
        } finally {
          this.loading = false
        }
      },
      nextPhoto() {
        this.currentPhotoIndex = 
          (this.currentPhotoIndex + 1) % this.photos.length
      },
      previousPhoto() {
        this.currentPhotoIndex = 
          (this.currentPhotoIndex - 1 + this.photos.length) % this.photos.length
      },
      selectPhoto(index) {
        this.currentPhotoIndex = index
      }
    },
    created() {
      this.fetchHousePhotos()
    },
    watch: {
      houseId() {
        // 如果 houseId 發生變化，重新獲取照片
        this.fetchHousePhotos()
      }
    }
  }