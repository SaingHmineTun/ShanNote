package it.saimao.shannote.viewmodel;

import androidx.lifecycle.ViewModel;

public class AddNoteViewModel extends ViewModel {
    private boolean hasUnsavedChanges = false;

    public void setHasUnsavedChanges(boolean hasUnsavedChanges) {
        this.hasUnsavedChanges = hasUnsavedChanges;
    }

    public boolean hasUnsavedChanges() {
        return hasUnsavedChanges;
    }
}
