import { createSlice } from "@reduxjs/toolkit"
// createSlice makes all action creators and reducers in the same file so no separation of logic is necessary

/***************STATE SLICE***************/

const pokeAPISlice = createSlice({
  name: "pokeAPI",
  initialState: {
    totalPokemonInPokedex: 0, // The number of pokemon accessible by pokeAPI. Updated on initial render
    nextFetchLink: "https://pokeapi.co/api/v2/pokemon/?offset=20&limit=20", // Generated by pokeAPI to fetch the next set of pokemon. Limit determines how many pokemon we fetch with one API call. Default is 20.
    pokemon: [], // All pokemon the user has looked at so far, and all their details. API calls only fetch the name of each pokemon
    loading: false
  },
  reducers: {
    toggleLoading(state) {
      state.loading = !state.loading
    },
    firstRender(state, action) {
      const { count, next, results } = action.payload
      state.totalPokemonInPokedex = count // createSlice uses IMMER under the hood so we can write as if we were mutating state but it will automatically convert it to an immutable statement
      state.nextFetchLink = next
      state.pokemon = results
      toggleLoading()
    },
    pokemonNames(state, action) {
      const { nextFetchLink, pokemon } = action.payload
      state.nextFetchLink = nextFetchLink
      state.pokemon.push(...pokemon)
    },
    pokemonDetails(state, action) {
      const { index, details } = action.payload // index is the pokemon's location in the array
      state.pokemon[index] = { ...state.pokemon[index], ...details } // add all the associated details of the pokemon from the API to the state
    },
    // SOME SEARCH REDUCER SHOULD GO HERE
    apiError(state, action) {
      const { message } = action.payload
      alert(message)
    }
  }
})

/***************EXPORTED ACTIONS & REDUCERS***************/

export default pokeAPISlice.reducer

export const {
  toggleLoading,
  firstRender,
  pokemonNames,
  pokemonDetails,
  apiError
} = pokeAPISlice.actions

// API REQUEST ACTIONS HANDLED WITH REDUX-THUNK MIDDLEWARE BUILT INTO REDUX TOOLKIT -->

/***************THUNKS***************/

export const getFirstRender = () => {
  return async dispatch => {
    dispatch(toggleLoading())
    // redux-thunk
    try {
      const apiResponse = await fetch("https://pokeapi.co/api/v2/pokemon") // fetches 20 pokemon names, nextFetchLink and totalPokemonInPokedex
      const firstRenderData = await apiResponse.json()
      dispatch(firstRender(firstRenderData))
      dispatch(getPokemonDetails(firstRenderData.results))
    } catch (e) {
      apiError(e.message)
    }
  }
}

export const getPokemonNames = nextFetchLink => {
  /* nextFetchLink is a string in our state which has an offset and a limit which 
      determines how many pokemon we fetch from the API at once, as well as where we 
      begin our fetch. For example offset=100&limit=50 begins our fetch at the 100th 
      pokemon and fetches the next 50 pokemon. */
  const link = nextFetchLink
  return async dispatch => {
    // redux-thunk
    try {
      const apiResponse = await fetch(link)
      const apiData = await apiResponse.json()
      const nextFetchLink = apiData.next // update the link for possible future api request
      const pokemon = apiData.results // this is an array con taining the names and urls for a number of pokemon === to the "limit" set by nextFetchLink
      dispatch(pokemonNames({ nextFetchLink, pokemon }))
    } catch (e) {
      apiError(e.message)
    }
  }
}

export const getPokemonDetails = pokemon => {
  // receives the array of pokemon names from getFirstRender or state
  return async dispatch => {
    try {
      // redux-thunk
      pokemon.forEach(async (currentPokemon, index) => {
        if (currentPokemon.id === undefined) {
          // if it does not have an ID then it hasn't been fetched yet
          const primaryDetailsRequest = await fetch(
            "https://pokeapi.co/api/v2/pokemon/" + currentPokemon.name
          )
          const {
            id,
            abilities,
            height,
            weight,
            sprites,
            stats,
            types
          } = await primaryDetailsRequest.json()
          const speciesInfoRequest = await fetch(
            "https://pokeapi.co/api/v2/pokemon-species/" + currentPokemon.name
          )
          const {
            capture_rate,
            color,
            habitat,
            flavor_text_entries
          } = await speciesInfoRequest.json()
          const description = flavor_text_entries.find(
            flavor => flavor.language.name === "en"
          )
          let modifiedHeight = height * 0.328084 // API doesn't include feet and inches punctuation so we create it here
          modifiedHeight = Math.round(modifiedHeight) + "'"
          let modifiedWeight = weight * 0.2204623
          modifiedWeight = Math.round(modifiedWeight) + " lbs" // API doesn't format weight so we must do it here
          const details = {
            id,
            abilities,
            height: modifiedHeight,
            weight: modifiedWeight,
            sprites,
            stats,
            types,
            capture_rate,
            color,
            habitat,
            description
          }
          dispatch(pokemonDetails({ index, details }))
        }
      })
      dispatch(toggleLoading()) // stop loading after the forEach loop completes
    } catch (e) {
      apiError(e.message)
    }
  }
}

export const getMorePokemon = nextFetchLink => {
  const link = nextFetchLink
  console.log(nextFetchLink)
  return async (dispatch, getState) => {
    dispatch(toggleLoading())
    try {
      const apiResponse = await fetch(link)
      const apiData = await apiResponse.json()
      const nextFetchLink = apiData.next // update the link for possible future api request
      const pokemon = apiData.results // this is an array con taining the names and urls for a number of pokemon === to the "limit" set by nextFetchLink
      console.log(nextFetchLink, pokemon)
      dispatch(pokemonNames({ nextFetchLink, pokemon }))
      dispatch(getPokemonDetails(getState().pokeAPI.pokemon))
    } catch (e) {
      apiError(e.message)
    }
  }
}

export const searchPokemon = pokemonName => {
  console.log("SEARCH POKEMON DISPATCHED: ", pokemonName)
  return async dispatch => {
    try {
      const apiResponse = await fetch(
        `https://pokeapi.co/api/v2/pokemon/${pokemonName}`
      )
      const pokemonDetails = await apiResponse.json()
      const pokemonIndex = pokemonDetails.id
      console.log(pokemonDetails, pokemonIndex)
      // some dispatch() should go here, to line 29 above
    } catch (e) {
      apiError(e.message)
    }
  }
}
