<template>
  <div>
    <div class="row form-row">
      <div class="col-6">
        <text-field field-id="streetName"
                    label="address.street-name.label"
                    placeholder="address.street-name.placeholder"
                    v-model="address.streetName"
                    :maxlength="50"
                    validation="required|max:50"></text-field>
      </div>
      <div class="col-md-6">
        <text-field field-id="houseNumber"
                    label="address.house-number.label"
                    placeholder="address.house-number.placeholder"
                    v-model="address.houseNumber"
                    :maxlength="10"
                    validation="required|max:10"></text-field>
      </div>
    </div>
    <div class="row form-row">
      <div class="col-md-6">
        <text-field field-id="zipCode"
                    label="address.zip-code.label"
                    placeholder="address.zip-code.placeholder"
                    v-model="address.zipCode"
                    :maxlength="10"
                    validation="required|max:10"></text-field>
      </div>
      <div class="col-md-6">
        <text-field field-id="city"
                    label="address.city.label"
                    placeholder="address.city.placeholder"
                    v-model="address.city"
                    :maxlength="50"
                    validation="required|max:50"></text-field>
      </div>
    </div>

    <div class="row form-row">
      <div class="col-md-6">
        <select-field
          :options="countries"
          v-model="address.country"
          :codelist="true"
          :translate="false"
          :clearable="false"
          field-id="country"
          label="address.country.label"
          validation="required"></select-field>
      </div>
      <div class="col-md-6" v-if="showRegion">
        <select-field
          :options="regions"
          :translate="false"
          v-model="address.region"
          field-id="region"
          label="region.label"></select-field>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import Vue from 'vue'
import Component from 'vue-class-component'
import textField from '@/modules/common/components/form/textField.vue'
import selectField from '@/modules/common/components/form/selectField.vue'
import { Prop } from 'vue-property-decorator'
import Address from '@/modules/common/domain/address'
import countries from '@/modules/common/values/countries'
import { State } from 'vuex-class'
import CodeList from '@/modules/common/domain/codeList'

@Component({
  components: { selectField, textField }
})
export default class AddressForm extends Vue {
  countries = countries
  @Prop({ type: Address, required: true }) address!: Address
  @Prop({ type: Boolean, default: true }) showRegion!: boolean
  @State('items', { namespace: 'region' }) regions!: Array<CodeList>

  created () {
    this.$store.dispatch('region/getAll')
  }
}
</script>
