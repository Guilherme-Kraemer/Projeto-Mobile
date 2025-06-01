class GetProductByBarcodeUseCase @Inject constructor(
    private val repository: MedicationRepositoryImpl // Usar implementação para barcode
) {
    suspend operator fun invoke(barcode: String): Result<Medication> {
        return repository.searchMedicationByBarcode(barcode)
    }
}