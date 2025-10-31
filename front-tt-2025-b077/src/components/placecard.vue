<template>
  <v-card
    class="place-card-vertical mb-4"
    elevation="3"
    rounded="lg"
    hover
    @click="handlePlaceClick"
  >
    <div class="place-image-container-vertical">
      <v-carousel
        v-if="place.images && place.images.length > 0"
        :height="180"
        :show-arrows="hasMultipleImages"
        :hide-delimiters="true"
        cycle
        class="place-carousel"
      >
        <v-carousel-item
          v-for="(image, index) in place.images"
          :key="index"
          :src="image.photoUrl"
          cover
        />
      </v-carousel>

      <div v-else class="default-image-container">
        <img :src="defaultImage" alt="Imagen por defecto" class="default-image" />
      </div>

      <div class="favorite-btn" @click.stop="handleToggleFavorite">
        <i
          :class="isFavorite ? 'mdi mdi-heart text-danger' : 'mdi mdi-heart-outline text-white'"
        ></i>
      </div>

      <div class="rating-badge" v-if="place.rating">
        <i class="mdi mdi-star text-warning"></i>
        <span class="rating-text">{{ place.rating.toFixed(1) }}</span>
      </div>
    </div>

    <v-card-text class="place-info-vertical">
      <v-card-title class="place-name pa-0 pb-2">{{ place.name }}</v-card-title>
      <p class="place-address mb-3">
        <i class="mdi mdi-map-marker me-1"></i>
        {{ shortAddress }}
      </p>
      <div class="place-details">
        <span v-if="placeTypeLabel" class="place-type">
          {{ placeTypeLabel }}
        </span>
        <span v-if="reviewsCount > 0" class="reviews-count"> {{ reviewsCount }} reseñas </span>
      </div>
    </v-card-text>
  </v-card>
</template>

<script>
export default {
  name: 'PlaceCard',
  props: {
    place: {
      type: Object,
      required: true,
    },
    isFavorite: {
      type: Boolean,
      default: false,
    },
    logoUrl: {
      type: String,
      default: '/logo-letras.png',
    },
  },
  emits: ['select-place', 'toggle-favorite'],
  computed: {
    hasMultipleImages() {
      return Array.isArray(this.place?.images) && this.place.images.length > 1
    },
    defaultImage() {
      return this.logoUrl
    },
    shortAddress() {
      if (!this.place.formattedAddress) return 'Dirección no disponible'
      const parts = this.place.formattedAddress.split(',')
      return parts.slice(0, 2).join(',').trim()
    },
    placeTypeLabel() {
      if (!this.place.placeTypes || this.place.placeTypes.length === 0) return null

      if (this.place.placeTypes.length === 1) {
        return this.getPlaceTypeTranslation(this.place.placeTypes[0])
      }

      const filteredTypes = this.place.placeTypes.filter(
        (type) => type !== 'point_of_interest' && type !== 'establishment',
      )

      const typesToShow = filteredTypes.length > 0 ? filteredTypes : this.place.placeTypes
      return typesToShow.map((type) => this.getPlaceTypeTranslation(type)).join(', ')
    },
    reviewsCount() {
      return this.place.reviews?.length || 0
    },
  },
  methods: {
    handlePlaceClick() {
      this.$emit('select-place', this.place)
    },
    handleToggleFavorite() {
      this.$emit('toggle-favorite', this.place)
    },
    getPlaceTypeTranslation(type) {
      const typeTranslations = {
        restaurant: 'Restaurante',
        cafe: 'Café',
        tourist_attraction: 'Atracción',
        shopping_mall: 'Centro Comercial',
        park: 'Parque',
        museum: 'Museo',
        bar: 'Bar',
        gym: 'Gimnasio',
        hospital: 'Hospital',
        gas_station: 'Gasolinera',
        pharmacy: 'Farmacia',
        establishment: 'Establecimiento',
        point_of_interest: 'Punto de Interés',
        lodging: 'Hotel',
        church: 'Iglesia',
        university: 'Universidad',
        store: 'Tienda',
        library: "Biblioteca",
      }
      return typeTranslations[type] || type.replace(/_/g, ' ')
    },
  },
}
</script>

<style scoped>
.place-card-vertical {
  cursor: pointer;
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
  max-width: 100%;
  overflow: hidden;
}

.place-card-vertical:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15) !important;
}

.place-image-container-vertical {
  position: relative;
  width: 100%;
  height: 180px;
  overflow: hidden;
}

.default-image-container {
  width: 100%;
  height: 100%;
}

.default-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.favorite-btn {
  position: absolute;
  top: 8px;
  left: 8px;
  background: rgba(0, 0, 0, 0.5);
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  backdrop-filter: blur(10px);
  z-index: 10;
}

.favorite-btn:hover {
  background: rgba(0, 0, 0, 0.7);
  transform: scale(1.1);
}

.favorite-btn i {
  font-size: 1rem;
  transition: all 0.2s ease;
}

.rating-badge {
  position: absolute;
  top: 8px;
  right: 8px;
  background: rgba(255, 255, 255, 0.95);
  padding: 4px 8px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  gap: 2px;
  font-size: 0.75rem;
  font-weight: 600;
  backdrop-filter: blur(10px);
}

.rating-text {
  color: #333;
}

.place-info-vertical {
  padding: 1rem;
  max-width: 100%;
  overflow: hidden;
}

.place-name {
  font-size: 1rem;
  font-weight: 600;
  color: #333;
  line-height: 1.2;
  word-wrap: break-word;
  overflow-wrap: break-word;
  overflow: hidden;
  max-width: 100%;
}

.place-address {
  font-size: 0.85rem;
  color: #666;
  line-height: 1.3;
  word-wrap: break-word;
  overflow-wrap: break-word;
  overflow: hidden;
  max-width: 100%;
}

.place-details {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.place-type {
  font-size: 0.75rem;
  background: #f8f9fa;
  color: #666;
  padding: 2px 8px;
  border-radius: 12px;
  text-transform: capitalize;
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.reviews-count {
  font-size: 0.7rem;
  color: #999;
}

@media (max-width: 480px) {
  .favorite-btn {
    width: 32px;
    height: 32px;
  }

  .favorite-btn i {
    font-size: 0.9rem;
  }
}
</style>
